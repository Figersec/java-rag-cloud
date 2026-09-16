package com.kailin.service.rag.vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kailin.api.KBException;
import com.kailin.api.RagKRMessage;
import com.kailin.config.rag.RagProperties;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.AddFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.HasCollectionReq;
import io.milvus.v2.service.collection.request.LoadCollectionReq;
import io.milvus.v2.service.vector.request.DeleteReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.SearchResp;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MilvusVectorStore {

    private final RagProperties ragProperties;
    private MilvusClientV2 client;

    public MilvusVectorStore(RagProperties ragProperties) {
        this.ragProperties = ragProperties;
    }

    @PostConstruct
    public void init() {
        try {
            client = new MilvusClientV2(ConnectConfig.builder()
                    .uri(ragProperties.getMilvus().getUri())
                    .build());
            ensureCollection();
            log.info("Milvus collection ready: {}", ragProperties.getMilvus().getCollection());
        } catch (Exception e) {
            log.warn("Milvus 暂不可用，问答/入库时会失败: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void close() {
        if (client != null) {
            client.close();
        }
    }

    public synchronized void insert(String kbId, String docId, List<String> chunkIds, List<List<Float>> vectors) {
        MilvusClientV2 milvus = requireClient();
        ensureCollection();
        List<JsonObject> rows = new ArrayList<>();
        for (int i = 0; i < chunkIds.size(); i++) {
            JsonObject row = new JsonObject();
            row.addProperty("id", chunkIds.get(i));
            row.addProperty("kb_id", kbId);
            row.addProperty("doc_id", docId);
            JsonArray embedding = new JsonArray();
            for (Float value : vectors.get(i)) {
                embedding.add(value);
            }
            row.add("embedding", embedding);
            rows.add(row);
        }
        try {
            milvus.insert(InsertReq.builder()
                    .collectionName(collection())
                    .data(rows)
                    .build());
        } catch (Exception e) {
            throw new KBException(RagKRMessage.MILVUS_FAILED, e.getMessage());
        }
    }

    public synchronized void deleteByDocId(String docId) {
        MilvusClientV2 milvus = requireClient();
        ensureCollection();
        try {
            milvus.delete(DeleteReq.builder()
                    .collectionName(collection())
                    .filter("doc_id == \"" + escape(docId) + "\"")
                    .build());
        } catch (Exception e) {
            throw new KBException(RagKRMessage.MILVUS_FAILED, e.getMessage());
        }
    }

    public List<VectorHit> search(String kbId, List<Float> queryVector, int topK) {
        MilvusClientV2 milvus = requireClient();
        ensureCollection();
        try {
            milvus.loadCollection(LoadCollectionReq.builder().collectionName(collection()).build());
            SearchResp resp = milvus.search(SearchReq.builder()
                    .collectionName(collection())
                    .data(List.of(new FloatVec(queryVector)))
                    .annsField("embedding")
                    .topK(topK)
                    .filter("kb_id == \"" + escape(kbId) + "\"")
                    .outputFields(List.of("id", "kb_id", "doc_id"))
                    .build());
            List<VectorHit> hits = new ArrayList<>();
            List<List<SearchResp.SearchResult>> results = resp.getSearchResults();
            if (results == null || results.isEmpty()) {
                return hits;
            }
            for (SearchResp.SearchResult result : results.get(0)) {
                VectorHit hit = new VectorHit();
                Object id = result.getId();
                hit.setChunkId(id == null ? null : String.valueOf(id));
                hit.setScore(result.getScore());
                Map<String, Object> entity = result.getEntity();
                if (entity != null) {
                    Object chunkId = entity.get("id");
                    if (chunkId != null) {
                        hit.setChunkId(String.valueOf(chunkId));
                    }
                    Object hitKbId = entity.get("kb_id");
                    if (hitKbId != null) {
                        hit.setKbId(String.valueOf(hitKbId));
                    }
                    Object docId = entity.get("doc_id");
                    if (docId != null) {
                        hit.setDocId(String.valueOf(docId));
                    }
                }
                hits.add(hit);
            }
            return hits;
        } catch (Exception e) {
            throw new KBException(RagKRMessage.MILVUS_FAILED, e.getMessage());
        }
    }

    private void ensureCollection() {
        MilvusClientV2 milvus = requireClient();
        Boolean exists = milvus.hasCollection(HasCollectionReq.builder()
                .collectionName(collection())
                .build());
        if (Boolean.TRUE.equals(exists)) {
            return;
        }
        CreateCollectionReq.CollectionSchema schema = milvus.createSchema();
        schema.addField(AddFieldReq.builder()
                .fieldName("id")
                .dataType(DataType.VarChar)
                .maxLength(64)
                .isPrimaryKey(true)
                .autoID(false)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("kb_id")
                .dataType(DataType.VarChar)
                .maxLength(64)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("doc_id")
                .dataType(DataType.VarChar)
                .maxLength(64)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("embedding")
                .dataType(DataType.FloatVector)
                .dimension(ragProperties.getEmbedding().getDimension())
                .build());
        IndexParam indexParam = IndexParam.builder()
                .fieldName("embedding")
                .indexType(IndexParam.IndexType.AUTOINDEX)
                .metricType(IndexParam.MetricType.COSINE)
                .build();
        milvus.createCollection(CreateCollectionReq.builder()
                .collectionName(collection())
                .collectionSchema(schema)
                .indexParams(List.of(indexParam))
                .build());
        milvus.loadCollection(LoadCollectionReq.builder().collectionName(collection()).build());
    }

    private MilvusClientV2 requireClient() {
        if (client == null) {
            try {
                client = new MilvusClientV2(ConnectConfig.builder()
                        .uri(ragProperties.getMilvus().getUri())
                        .build());
            } catch (Exception e) {
                throw new KBException(RagKRMessage.MILVUS_FAILED, e.getMessage());
            }
        }
        return client;
    }

    private String collection() {
        return ragProperties.getMilvus().getCollection();
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

package com.kailin.service.rag.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kailin.api.KBException;
import com.kailin.api.RagKRMessage;
import com.kailin.config.rag.RagProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiCompatibleClient {

    private final RagProperties ragProperties;
    private final RestTemplate ragRestTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenAiCompatibleClient(RagProperties ragProperties,
                                  @Qualifier("ragRestTemplate") RestTemplate ragRestTemplate) {
        this.ragProperties = ragProperties;
        this.ragRestTemplate = ragRestTemplate;
    }

    public List<List<Float>> embed(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return List.of();
        }
        RagProperties.Embedding embedding = ragProperties.getEmbedding();
        ensureApiKey(embedding.getApiKey());
        List<List<Float>> all = new ArrayList<>(texts.size());
        int batchSize = Math.max(1, embedding.getBatchSize());
        for (int i = 0; i < texts.size(); i += batchSize) {
            List<String> batch = texts.subList(i, Math.min(texts.size(), i + batchSize));
            all.addAll(embedBatch(batch, embedding));
        }
        return all;
    }

    public String chat(String systemPrompt, String userPrompt) {
        RagProperties.Chat chat = ragProperties.getChat();
        ensureApiKey(chat.getApiKey());
        Map<String, Object> body = new HashMap<>();
        body.put("model", chat.getModel());
        body.put("temperature", chat.getTemperature());
        body.put("max_tokens", chat.getMaxTokens());
        body.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
        ));
        try {
            JsonNode root = postJson(joinUrl(chat.getBaseUrl(), "/chat/completions"), chat.getApiKey(), body);
            JsonNode content = root.path("choices").path(0).path("message").path("content");
            if (content.isMissingNode() || content.isNull()) {
                throw new KBException(RagKRMessage.LLM_FAILED, "模型未返回内容");
            }
            return content.asText();
        } catch (KBException e) {
            throw e;
        } catch (Exception e) {
            throw new KBException(RagKRMessage.LLM_FAILED, e.getMessage());
        }
    }

    private List<List<Float>> embedBatch(List<String> batch, RagProperties.Embedding embedding) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", embedding.getModel());
        body.put("input", batch.size() == 1 ? batch.get(0) : batch);
        body.put("dimensions", embedding.getDimension());
        body.put("encoding_format", "float");
        try {
            JsonNode root = postJson(joinUrl(embedding.getBaseUrl(), "/embeddings"), embedding.getApiKey(), body);
            JsonNode data = root.path("data");
            if (!data.isArray() || data.isEmpty()) {
                throw new KBException(RagKRMessage.EMBEDDING_FAILED, "向量接口未返回 data");
            }
            List<List<Float>> vectors = new ArrayList<>();
            for (JsonNode item : data) {
                List<Float> vector = new ArrayList<>();
                for (JsonNode n : item.path("embedding")) {
                    vector.add((float) n.asDouble());
                }
                if (vector.size() != embedding.getDimension()) {
                    throw new KBException(RagKRMessage.EMBEDDING_FAILED,
                            "向量维度为 " + vector.size() + "，配置为 " + embedding.getDimension());
                }
                vectors.add(vector);
            }
            return vectors;
        } catch (KBException e) {
            throw e;
        } catch (Exception e) {
            throw new KBException(RagKRMessage.EMBEDDING_FAILED, e.getMessage());
        }
    }

    private JsonNode postJson(String url, String apiKey, Map<String, Object> body) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        ResponseEntity<String> response = ragRestTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
        if (!response.getStatusCode().is2xxSuccessful() || StringUtils.isBlank(response.getBody())) {
            throw new IllegalStateException("HTTP " + response.getStatusCode().value());
        }
        return objectMapper.readTree(response.getBody());
    }

    private void ensureApiKey(String apiKey) {
        if (StringUtils.isBlank(apiKey)) {
            throw new KBException(RagKRMessage.API_KEY_MISSING);
        }
    }

    private String joinUrl(String baseUrl, String path) {
        String base = StringUtils.removeEnd(StringUtils.defaultString(baseUrl), "/");
        return base + path;
    }
}

package com.kailin.config.rag;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rag")
public class RagProperties {

    private Embedding embedding = new Embedding();
    private Chat chat = new Chat();
    private Milvus milvus = new Milvus();
    private Chunk chunk = new Chunk();
    private Storage storage = new Storage();

    @Data
    public static class Embedding {
        private String baseUrl;
        private String apiKey;
        private String model;
        private int dimension;
        private int batchSize;
    }

    @Data
    public static class Chat {
        private String baseUrl;
        private String apiKey;
        private String model;
        private double temperature;
        private int maxTokens;
    }

    @Data
    public static class Milvus {
        private String uri = "http://127.0.0.1:19530";
        private String collection = "rag_chunk";
    }

    @Data
    public static class Chunk {
        private int size = 500;
        private int overlap = 80;
    }

    @Data
    public static class Storage {
        private String localDir = System.getProperty("user.home") + "/java-rag-files";
    }
}

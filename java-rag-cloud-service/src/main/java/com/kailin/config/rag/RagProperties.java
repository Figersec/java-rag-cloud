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
        private String baseUrl = "https://api.siliconflow.cn/v1";
        private String apiKey = "";
        private String model = "BAAI/bge-m3";
        private int dimension = 1024;
        private int batchSize = 16;
    }

    @Data
    public static class Chat {
        private String baseUrl = "https://api.siliconflow.cn/v1";
        private String apiKey = "";
        private String model = "Qwen/Qwen2.5-7B-Instruct";
        private double temperature = 0.2;
        private int maxTokens = 1024;
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

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
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

    public void chatStream(String systemPrompt, String userPrompt,
                           java.util.function.Consumer<String> onReasoning,
                           java.util.function.Consumer<String> onContent) {
        RagProperties.Chat chat = ragProperties.getChat();
        ensureApiKey(chat.getApiKey());
        Map<String, Object> body = new HashMap<>();
        body.put("model", chat.getModel());
        body.put("temperature", chat.getTemperature());
        body.put("max_tokens", chat.getMaxTokens());
        body.put("stream", true);
        body.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
        ));
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(joinUrl(chat.getBaseUrl(), "/chat/completions")))
                    .timeout(Duration.ofSeconds(180))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + chat.getApiKey())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.ACCEPT, "text/event-stream")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                    .build();
            HttpResponse<InputStream> response = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() / 100 != 2) {
                String err = new String(response.body().readAllBytes(), StandardCharsets.UTF_8);
                throw new KBException(RagKRMessage.LLM_FAILED, "HTTP " + response.statusCode() + " " + err);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith("data:")) {
                        continue;
                    }
                    String data = line.substring(5).trim();
                    if (data.isEmpty()) {
                        continue;
                    }
                    if ("[DONE]".equals(data)) {
                        break;
                    }
                    JsonNode delta = objectMapper.readTree(data).path("choices").path(0).path("delta");
                    emitText(delta, List.of("reasoning_content", "reasoning", "thinking", "reasoning_text"), onReasoning);
                    emitText(delta, List.of("content"), onContent);
                }
            }
        } catch (KBException e) {
            throw e;
        } catch (Exception e) {
            throw new KBException(RagKRMessage.LLM_FAILED, e.getMessage());
        }
    }

    private void emitText(JsonNode delta, List<String> fields, java.util.function.Consumer<String> consumer) {
        if (consumer == null) {
            return;
        }
        for (String field : fields) {
            JsonNode node = delta.path(field);
            if (!node.isMissingNode() && !node.isNull() && node.isTextual() && !node.asText().isEmpty()) {
                consumer.accept(node.asText());
                return;
            }
        }
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

    public String ocrImage(byte[] imageBytes) {
        RagProperties.Ocr ocr = ragProperties.getOcr();
        String baseUrl = StringUtils.defaultIfBlank(ocr.getBaseUrl(), ragProperties.getEmbedding().getBaseUrl());
        String apiKey = StringUtils.defaultIfBlank(ocr.getApiKey(), ragProperties.getEmbedding().getApiKey());
        ensureApiKey(apiKey);
        String dataUrl = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
        Map<String, Object> body = new HashMap<>();
        body.put("model", StringUtils.defaultIfBlank(ocr.getModel(), "qwen-vl-ocr"));
        body.put("temperature", 0);
        body.put("messages", List.of(Map.of(
                "role", "user",
                "content", List.of(
                        Map.of("type", "image_url", "image_url", Map.of("url", dataUrl)),
                        Map.of("type", "text", "text", "请提取图片中的全部文字，保持阅读顺序，只输出文字，不要解释。")
                )
        )));
        try {
            JsonNode root = postJson(joinUrl(baseUrl, "/chat/completions"), apiKey, body);
            JsonNode content = root.path("choices").path(0).path("message").path("content");
            if (content.isMissingNode() || content.isNull()) {
                throw new KBException(RagKRMessage.DOCUMENT_PARSE_FAILED, "OCR 未返回内容");
            }
            if (content.isTextual()) {
                return content.asText();
            }
            if (content.isArray()) {
                StringBuilder text = new StringBuilder();
                for (JsonNode part : content) {
                    if (part.has("text")) {
                        text.append(part.path("text").asText());
                    } else if (part.isTextual()) {
                        text.append(part.asText());
                    }
                }
                return text.toString();
            }
            return content.asText();
        } catch (KBException e) {
            throw e;
        } catch (Exception e) {
            throw new KBException(RagKRMessage.DOCUMENT_PARSE_FAILED, "OCR 失败: " + e.getMessage());
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
            throw new IllegalStateException("HTTP " + response.getStatusCode().value() + " " + StringUtils.abbreviate(response.getBody(), 400));
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

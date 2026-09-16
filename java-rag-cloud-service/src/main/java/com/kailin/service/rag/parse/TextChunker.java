package com.kailin.service.rag.parse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TextChunker {

    public List<String> chunk(String text, int size, int overlap) {
        String normalized = StringUtils.defaultString(text).replace("\r\n", "\n").trim();
        if (normalized.isEmpty() || size <= 0) {
            return List.of();
        }
        int step = Math.max(1, size - Math.max(0, overlap));
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < normalized.length(); i += step) {
            int end = Math.min(normalized.length(), i + size);
            String piece = normalized.substring(i, end).trim();
            if (!piece.isEmpty()) {
                chunks.add(piece);
            }
            if (end >= normalized.length()) {
                break;
            }
        }
        return chunks;
    }
}

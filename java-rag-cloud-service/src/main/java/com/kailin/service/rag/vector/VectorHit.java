package com.kailin.service.rag.vector;

import lombok.Data;

@Data
public class VectorHit {

    private String chunkId;
    private String kbId;
    private String docId;
    private Float score;
}

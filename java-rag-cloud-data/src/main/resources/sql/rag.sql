CREATE TABLE IF NOT EXISTS rag_kb (
    id            VARCHAR(64)  NOT NULL PRIMARY KEY,
    name          VARCHAR(128) NOT NULL,
    description   VARCHAR(512) DEFAULT NULL,
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_delete     TINYINT      DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库';

CREATE TABLE IF NOT EXISTS rag_document (
    id            VARCHAR(64)  NOT NULL PRIMARY KEY,
    kb_id         VARCHAR(64)  NOT NULL,
    file_name     VARCHAR(255) NOT NULL,
    file_type     VARCHAR(32)  NOT NULL,
    file_path     VARCHAR(512) NOT NULL,
    status        VARCHAR(32)  NOT NULL,
    chunk_count   INT          DEFAULT 0,
    error_msg     VARCHAR(1024) DEFAULT NULL,
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_delete     TINYINT      DEFAULT 0,
    INDEX idx_rag_document_kb (kb_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库文档';

CREATE TABLE IF NOT EXISTS rag_chunk (
    id            VARCHAR(64)  NOT NULL PRIMARY KEY,
    kb_id         VARCHAR(64)  NOT NULL,
    doc_id        VARCHAR(64)  NOT NULL,
    chunk_index   INT          NOT NULL,
    content       MEDIUMTEXT   NOT NULL,
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    is_delete     TINYINT      DEFAULT 0,
    INDEX idx_rag_chunk_kb (kb_id),
    INDEX idx_rag_chunk_doc (doc_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档切片';

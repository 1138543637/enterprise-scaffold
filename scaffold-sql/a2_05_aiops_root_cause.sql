SET NAMES utf8mb4;

USE enterprise_scaffold;

CREATE TABLE IF NOT EXISTS aiops_root_cause_record (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    analysis_code VARCHAR(64) NOT NULL COMMENT '分析编码',
    alert_event_id BIGINT NOT NULL COMMENT '告警事件ID',
    event_code VARCHAR(64) NOT NULL COMMENT '告警事件编码',
    resource_id BIGINT DEFAULT NULL COMMENT '资源ID',
    resource_code VARCHAR(64) DEFAULT NULL COMMENT '资源编码',
    resource_name VARCHAR(100) DEFAULT NULL COMMENT '资源名称',
    resource_type VARCHAR(32) DEFAULT NULL COMMENT '资源类型',
    ip_addr VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    root_cause_type VARCHAR(64) NOT NULL COMMENT '根因类型',
    root_cause_desc VARCHAR(500) DEFAULT NULL COMMENT '根因描述',
    evidence TEXT COMMENT '分析证据',
    suggestion TEXT COMMENT '处理建议',
    confidence_score INT NOT NULL DEFAULT 0 COMMENT '置信度，0到100',
    analysis_time DATETIME NOT NULL COMMENT '分析时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态，0有效，1无效',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除，0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_analysis_code (analysis_code),
    UNIQUE KEY uk_alert_event_id (alert_event_id),
    KEY idx_event_code (event_code),
    KEY idx_resource_id (resource_id),
    KEY idx_resource_code (resource_code),
    KEY idx_resource_type (resource_type),
    KEY idx_ip_addr (ip_addr),
    KEY idx_root_cause_type (root_cause_type),
    KEY idx_analysis_time (analysis_time),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AIOps根因分析记录表';

SET NAMES utf8mb4;

USE enterprise_scaffold;

CREATE TABLE IF NOT EXISTS aiops_alert_rule (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    rule_code VARCHAR(64) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    resource_type VARCHAR(32) NOT NULL COMMENT '资源类型：SERVER/DATABASE/MIDDLEWARE/NETWORK',
    metric_type VARCHAR(32) NOT NULL COMMENT '指标类型：CPU/MEMORY/DISK/NETWORK/MYSQL/REDIS',
    compare_operator VARCHAR(16) NOT NULL COMMENT '比较符：GT/GE/LT/LE/EQ',
    threshold_value DECIMAL(18,4) NOT NULL COMMENT '阈值',
    alert_level TINYINT NOT NULL COMMENT '告警级别：1一般，2重要，3严重',
    alert_title VARCHAR(200) NOT NULL COMMENT '告警标题',
    alert_content VARCHAR(500) DEFAULT NULL COMMENT '告警内容',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0启用，1停用',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_rule_code (rule_code),
    KEY idx_resource_type (resource_type),
    KEY idx_metric_type (metric_type),
    KEY idx_alert_level (alert_level),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AIOps告警规则表';

CREATE TABLE IF NOT EXISTS aiops_alert_event (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    event_code VARCHAR(64) NOT NULL COMMENT '事件编码',
    rule_id BIGINT NOT NULL COMMENT '规则ID',
    rule_code VARCHAR(64) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    metric_data_id BIGINT NOT NULL COMMENT '指标数据ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    resource_code VARCHAR(64) NOT NULL COMMENT '资源编码',
    resource_name VARCHAR(100) NOT NULL COMMENT '资源名称',
    resource_type VARCHAR(32) NOT NULL COMMENT '资源类型',
    ip_addr VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    metric_code VARCHAR(64) NOT NULL COMMENT '指标编码',
    metric_name VARCHAR(100) NOT NULL COMMENT '指标名称',
    metric_type VARCHAR(32) NOT NULL COMMENT '指标类型',
    metric_value DECIMAL(18,4) NOT NULL COMMENT '指标值',
    threshold_value DECIMAL(18,4) NOT NULL COMMENT '阈值',
    compare_operator VARCHAR(16) NOT NULL COMMENT '比较符',
    alert_level TINYINT NOT NULL COMMENT '告警级别：1一般，2重要，3严重',
    alert_title VARCHAR(200) NOT NULL COMMENT '告警标题',
    alert_content VARCHAR(500) DEFAULT NULL COMMENT '告警内容',
    alert_time DATETIME NOT NULL COMMENT '告警时间',
    handle_status TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态：0未处理，1已派单，2已关闭',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0有效，1无效',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_event_code (event_code),
    UNIQUE KEY uk_rule_metric_data (rule_id, metric_data_id),
    KEY idx_rule_id (rule_id),
    KEY idx_metric_data_id (metric_data_id),
    KEY idx_resource_id (resource_id),
    KEY idx_resource_code (resource_code),
    KEY idx_resource_type (resource_type),
    KEY idx_metric_type (metric_type),
    KEY idx_alert_level (alert_level),
    KEY idx_alert_time (alert_time),
    KEY idx_handle_status (handle_status),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AIOps告警事件表';

CREATE TABLE IF NOT EXISTS aiops_work_order (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    work_order_code VARCHAR(64) NOT NULL COMMENT '工单编码',
    alert_event_id BIGINT NOT NULL COMMENT '告警事件ID',
    event_code VARCHAR(64) NOT NULL COMMENT '告警事件编码',
    alert_level TINYINT NOT NULL COMMENT '告警级别：1一般，2重要，3严重',
    work_order_title VARCHAR(200) NOT NULL COMMENT '工单标题',
    work_order_content VARCHAR(500) DEFAULT NULL COMMENT '工单内容',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    resource_code VARCHAR(64) NOT NULL COMMENT '资源编码',
    resource_name VARCHAR(100) NOT NULL COMMENT '资源名称',
    resource_type VARCHAR(32) NOT NULL COMMENT '资源类型',
    ip_addr VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    metric_code VARCHAR(64) NOT NULL COMMENT '指标编码',
    metric_name VARCHAR(100) NOT NULL COMMENT '指标名称',
    metric_type VARCHAR(32) NOT NULL COMMENT '指标类型',
    order_status TINYINT NOT NULL DEFAULT 0 COMMENT '工单状态：0待处理，1处理中，2已处理，3已关闭',
    handler_user_id BIGINT DEFAULT NULL COMMENT '处理人用户ID',
    handler_username VARCHAR(64) DEFAULT NULL COMMENT '处理人用户名',
    handle_time DATETIME DEFAULT NULL COMMENT '处理时间',
    handle_result VARCHAR(1000) DEFAULT NULL COMMENT '处理结果',
    close_user_id BIGINT DEFAULT NULL COMMENT '关闭人用户ID',
    close_username VARCHAR(64) DEFAULT NULL COMMENT '关闭人用户名',
    close_time DATETIME DEFAULT NULL COMMENT '关闭时间',
    close_result VARCHAR(1000) DEFAULT NULL COMMENT '关闭结果',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0有效，1无效',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_work_order_code (work_order_code),
    UNIQUE KEY uk_alert_event_id (alert_event_id),
    KEY idx_event_code (event_code),
    KEY idx_resource_id (resource_id),
    KEY idx_resource_code (resource_code),
    KEY idx_resource_type (resource_type),
    KEY idx_metric_type (metric_type),
    KEY idx_alert_level (alert_level),
    KEY idx_order_status (order_status),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AIOps运维工单表';

INSERT INTO aiops_alert_rule (rule_code, rule_name, resource_type, metric_type, compare_operator, threshold_value, alert_level, alert_title, alert_content, status, create_by, create_time, deleted, remark)
SELECT 'AIOPS-RULE-CPU-001', '服务器CPU使用率过高', 'SERVER', 'CPU', 'GE', 80.0000, 2, '服务器CPU使用率过高', '服务器CPU使用率超过阈值，请检查进程负载和系统资源。', 0, 'system', NOW(), 0, 'A2-04 初始化规则'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM aiops_alert_rule WHERE rule_code = 'AIOPS-RULE-CPU-001');
INSERT INTO aiops_alert_rule (rule_code, rule_name, resource_type, metric_type, compare_operator, threshold_value, alert_level, alert_title, alert_content, status, create_by, create_time, deleted, remark)
SELECT 'AIOPS-RULE-MEM-001', '服务器内存使用率过高', 'SERVER', 'MEMORY', 'GE', 85.0000, 2, '服务器内存使用率过高', '服务器内存使用率超过阈值，请检查内存占用和缓存情况。', 0, 'system', NOW(), 0, 'A2-04 初始化规则'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM aiops_alert_rule WHERE rule_code = 'AIOPS-RULE-MEM-001');
INSERT INTO aiops_alert_rule (rule_code, rule_name, resource_type, metric_type, compare_operator, threshold_value, alert_level, alert_title, alert_content, status, create_by, create_time, deleted, remark)
SELECT 'AIOPS-RULE-DISK-001', '服务器磁盘使用率过高', 'SERVER', 'DISK', 'GE', 90.0000, 3, '服务器磁盘使用率过高', '服务器磁盘使用率超过严重阈值，请尽快清理磁盘或扩容。', 0, 'system', NOW(), 0, 'A2-04 初始化规则'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM aiops_alert_rule WHERE rule_code = 'AIOPS-RULE-DISK-001');
INSERT INTO aiops_alert_rule (rule_code, rule_name, resource_type, metric_type, compare_operator, threshold_value, alert_level, alert_title, alert_content, status, create_by, create_time, deleted, remark)
SELECT 'AIOPS-RULE-MYSQL-001', 'MySQL响应时间过高', 'DATABASE', 'MYSQL', 'GE', 200.0000, 3, 'MySQL响应时间过高', '数据库响应时间超过阈值，请检查慢 SQL、连接数和磁盘 IO。', 0, 'system', NOW(), 0, 'A2-04 初始化规则'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM aiops_alert_rule WHERE rule_code = 'AIOPS-RULE-MYSQL-001');
INSERT INTO aiops_alert_rule (rule_code, rule_name, resource_type, metric_type, compare_operator, threshold_value, alert_level, alert_title, alert_content, status, create_by, create_time, deleted, remark)
SELECT 'AIOPS-RULE-REDIS-001', 'Redis内存使用率过高', 'MIDDLEWARE', 'REDIS', 'GE', 80.0000, 2, 'Redis内存使用率过高', 'Redis 内存使用率超过阈值，请检查缓存热点和淘汰策略。', 0, 'system', NOW(), 0, 'A2-04 初始化规则'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM aiops_alert_rule WHERE rule_code = 'AIOPS-RULE-REDIS-001');
INSERT INTO aiops_alert_rule (rule_code, rule_name, resource_type, metric_type, compare_operator, threshold_value, alert_level, alert_title, alert_content, status, create_by, create_time, deleted, remark)
SELECT 'AIOPS-RULE-NET-001', '网络设备流量异常', 'NETWORK', 'NETWORK', 'GE', 80.0000, 2, '网络设备流量异常', '网络设备流量指标超过阈值，请检查链路带宽和设备状态。', 0, 'system', NOW(), 0, 'A2-04 初始化规则'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM aiops_alert_rule WHERE rule_code = 'AIOPS-RULE-NET-001');

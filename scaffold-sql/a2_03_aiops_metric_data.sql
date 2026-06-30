SET NAMES utf8mb4;

USE enterprise_scaffold;

CREATE TABLE IF NOT EXISTS aiops_metric_data (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID，逻辑关联 aiops_resource.id',
    resource_code VARCHAR(64) NOT NULL COMMENT '资源编码',
    resource_name VARCHAR(100) NOT NULL COMMENT '资源名称',
    resource_type VARCHAR(32) NOT NULL COMMENT '资源类型：SERVER / DATABASE / MIDDLEWARE / NETWORK',
    ip_addr VARCHAR(64) DEFAULT NULL COMMENT '资源IP地址',
    metric_code VARCHAR(64) NOT NULL COMMENT '指标编码，例如 CPU_USAGE',
    metric_name VARCHAR(100) NOT NULL COMMENT '指标名称，例如 CPU使用率',
    metric_type VARCHAR(32) NOT NULL COMMENT '指标类型：CPU / MEMORY / DISK / NETWORK / MYSQL / REDIS',
    metric_value DECIMAL(18,4) NOT NULL COMMENT '指标值',
    metric_unit VARCHAR(32) DEFAULT NULL COMMENT '指标单位，例如 %、ms、MB、count',
    threshold_value DECIMAL(18,4) DEFAULT NULL COMMENT '告警阈值',
    alarm_flag TINYINT NOT NULL DEFAULT 0 COMMENT '是否触发告警：0否，1是',
    collect_time DATETIME NOT NULL COMMENT '采集时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常，1异常',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_resource_id (resource_id),
    KEY idx_resource_code (resource_code),
    KEY idx_resource_type (resource_type),
    KEY idx_ip_addr (ip_addr),
    KEY idx_metric_code (metric_code),
    KEY idx_metric_type (metric_type),
    KEY idx_collect_time (collect_time),
    KEY idx_alarm_flag (alarm_flag),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AIOps指标采集数据表';

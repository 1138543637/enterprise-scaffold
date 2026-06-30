SET NAMES utf8mb4;

USE enterprise_scaffold;

CREATE TABLE IF NOT EXISTS aiops_resource (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    resource_code VARCHAR(64) NOT NULL COMMENT '资源编码',
    resource_name VARCHAR(100) NOT NULL COMMENT '资源名称',
    resource_type VARCHAR(32) NOT NULL COMMENT '资源类型：SERVER服务器、DATABASE数据库、MIDDLEWARE中间件、NETWORK网络设备',
    ip_addr VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    port INT DEFAULT NULL COMMENT '端口',
    env_type VARCHAR(32) DEFAULT NULL COMMENT '环境类型：DEV开发、TEST测试、PROD生产',
    system_name VARCHAR(100) DEFAULT NULL COMMENT '所属系统',
    owner_name VARCHAR(64) DEFAULT NULL COMMENT '负责人',
    collect_enabled TINYINT NOT NULL DEFAULT 0 COMMENT '是否启用采集：0启用，1停止',
    last_collect_time DATETIME DEFAULT NULL COMMENT '最后采集时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常，1停用，2异常',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_resource_code (resource_code),
    KEY idx_resource_type (resource_type),
    KEY idx_ip_addr (ip_addr),
    KEY idx_env_type (env_type),
    KEY idx_system_name (system_name),
    KEY idx_collect_enabled (collect_enabled),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AIOps资源台账表';

INSERT IGNORE INTO aiops_resource (
    resource_code,
    resource_name,
    resource_type,
    ip_addr,
    port,
    env_type,
    system_name,
    owner_name,
    collect_enabled,
    last_collect_time,
    status,
    create_by,
    create_time,
    remark
) VALUES
('AIOPS-SERVER-001', '政务云应用服务器一号', 'SERVER', '10.10.1.11', 22, 'PROD', '政务云统一门户', 'aiops_engineer', 0, NOW(), 0, 'admin', NOW(), 'A2-02 初始化服务器资源'),
('AIOPS-SERVER-002', '运营商业务服务器一号', 'SERVER', '10.10.1.12', 22, 'PROD', '运营商云网监控平台', 'aiops_engineer', 0, NOW(), 0, 'admin', NOW(), 'A2-02 初始化服务器资源'),
('AIOPS-MYSQL-001', '核心业务 MySQL 数据库', 'DATABASE', '10.10.2.21', 3306, 'PROD', '核心业务系统', 'dba_admin', 0, NOW(), 0, 'admin', NOW(), 'A2-02 初始化数据库资源'),
('AIOPS-REDIS-001', '统一缓存 Redis 中间件', 'MIDDLEWARE', '10.10.3.31', 6379, 'PROD', '统一缓存平台', 'middleware_admin', 0, NOW(), 0, 'admin', NOW(), 'A2-02 初始化中间件资源'),
('AIOPS-SWITCH-001', '核心交换机一号', 'NETWORK', '10.10.4.41', 161, 'PROD', '数据中心网络', 'network_admin', 0, NOW(), 0, 'admin', NOW(), 'A2-02 初始化网络设备资源');

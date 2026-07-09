SET NAMES utf8mb4;

USE enterprise_scaffold;

CREATE TABLE IF NOT EXISTS iam_rate_limit_rule (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  rule_code VARCHAR(64) NOT NULL COMMENT '规则编码',
  rule_name VARCHAR(128) NOT NULL COMMENT '规则名称',
  request_uri VARCHAR(255) NOT NULL COMMENT '接口路径',
  request_method VARCHAR(16) NOT NULL COMMENT '请求方法：GET、POST、PUT、DELETE',
  limit_dimension VARCHAR(32) NOT NULL DEFAULT 'IP' COMMENT '限流维度：GLOBAL 全局，USER 用户，IP 客户端IP',
  limit_window_seconds INT NOT NULL DEFAULT 60 COMMENT '限流时间窗口，单位秒',
  max_requests INT NOT NULL DEFAULT 60 COMMENT '窗口内最大请求次数',
  limit_action VARCHAR(32) NOT NULL DEFAULT 'WARN' COMMENT '命中动作：WARN 仅告警，REJECT 拒绝',
  enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0 停用，1 启用',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '业务状态：0 正常，1 禁用',
  create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
  remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_rate_limit_rule_code (rule_code),
  KEY idx_rate_limit_request_uri (request_uri),
  KEY idx_rate_limit_request_method (request_method),
  KEY idx_rate_limit_dimension (limit_dimension),
  KEY idx_rate_limit_enabled (enabled),
  KEY idx_rate_limit_deleted (deleted),
  KEY idx_rate_limit_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='IAM接口限流规则表';

INSERT INTO iam_rate_limit_rule (
  rule_code,
  rule_name,
  request_uri,
  request_method,
  limit_dimension,
  limit_window_seconds,
  max_requests,
  limit_action,
  enabled,
  status,
  create_by,
  create_time,
  update_by,
  update_time,
  deleted,
  remark
) VALUES
('RL-IAM-LOGIN-RISK-DETECT', '异常登录检测接口限流', '/api/iam/login-risks/detect', 'POST', 'USER', 60, 5, 'WARN', 1, 0, 'admin', NOW(), 'admin', NOW(), 0, '演示规则：同一用户 60 秒内最多模拟 5 次异常登录检测请求'),
('RL-IAM-ACCESS-LOG-PAGE', '接口访问日志分页限流', '/api/iam/access-logs/page', 'GET', 'IP', 60, 30, 'WARN', 1, 0, 'admin', NOW(), 'admin', NOW(), 0, '演示规则：同一 IP 60 秒内最多模拟 30 次访问日志分页查询'),
('RL-IAM-RATE-LIMIT-PAGE', '限流规则分页接口限流', '/api/iam/rate-limit-rules/page', 'GET', 'IP', 60, 60, 'WARN', 1, 0, 'admin', NOW(), 'admin', NOW(), 0, '演示规则：同一 IP 60 秒内最多模拟 60 次限流规则分页查询')
ON DUPLICATE KEY UPDATE
  rule_name = VALUES(rule_name),
  request_uri = VALUES(request_uri),
  request_method = VALUES(request_method),
  limit_dimension = VALUES(limit_dimension),
  limit_window_seconds = VALUES(limit_window_seconds),
  max_requests = VALUES(max_requests),
  limit_action = VALUES(limit_action),
  enabled = VALUES(enabled),
  status = VALUES(status),
  update_by = VALUES(update_by),
  update_time = NOW(),
  deleted = 0,
  remark = VALUES(remark);

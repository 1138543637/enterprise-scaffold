SET NAMES utf8mb4;

USE enterprise_scaffold;

CREATE TABLE IF NOT EXISTS `iam_security_policy` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `policy_code` VARCHAR(64) NOT NULL COMMENT '策略编码',
  `policy_name` VARCHAR(100) NOT NULL COMMENT '策略名称',
  `policy_type` VARCHAR(32) NOT NULL COMMENT '策略类型：LOGIN 登录安全，RATE_LIMIT 接口限流，RISK 风险处理，AUDIT 审计保留',
  `policy_level` TINYINT NOT NULL DEFAULT 1 COMMENT '策略等级：1 低，2 中，3 高',
  `policy_value` VARCHAR(128) NOT NULL COMMENT '策略值',
  `policy_unit` VARCHAR(32) DEFAULT NULL COMMENT '策略单位：COUNT 次，SECOND 秒，DAY 天，LEVEL 等级',
  `effective_scope` VARCHAR(32) NOT NULL DEFAULT 'GLOBAL' COMMENT '生效范围：GLOBAL 全局，USER 用户，IP 客户端IP，API 接口',
  `enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0 停用，1 启用',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '业务状态：0 正常，1 禁用',
  `create_by` VARCHAR(64) DEFAULT 'system' COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT 'system' COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_iam_security_policy_code` (`policy_code`),
  KEY `idx_iam_security_policy_type` (`policy_type`),
  KEY `idx_iam_security_policy_level` (`policy_level`),
  KEY `idx_iam_security_policy_enabled` (`enabled`),
  KEY `idx_iam_security_policy_status` (`status`),
  KEY `idx_iam_security_policy_deleted` (`deleted`),
  KEY `idx_iam_security_policy_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IAM安全策略配置表';

INSERT INTO `iam_security_policy` (
  `policy_code`,
  `policy_name`,
  `policy_type`,
  `policy_level`,
  `policy_value`,
  `policy_unit`,
  `effective_scope`,
  `enabled`,
  `status`,
  `create_by`,
  `update_by`,
  `remark`
) VALUES
('SP-LOGIN-FAIL-THRESHOLD', '登录失败次数阈值', 'LOGIN', 3, '5', 'COUNT', 'USER', 1, 0, 'system', 'system', '同一用户在统计窗口内登录失败达到该次数后应进入异常登录风险检测。'),
('SP-LOGIN-FAIL-WINDOW', '登录失败统计窗口', 'LOGIN', 2, '600', 'SECOND', 'USER', 1, 0, 'system', 'system', '异常登录检测使用的登录失败统计时间窗口，单位为秒。'),
('SP-RATE-LIMIT-DEFAULT-MAX', '默认接口限流次数', 'RATE_LIMIT', 2, '60', 'COUNT', 'API', 1, 0, 'system', 'system', '接口未配置独立规则时可参考的默认最大请求次数，本阶段只配置策略，不做真实拦截。'),
('SP-RATE-LIMIT-DEFAULT-WINDOW', '默认接口限流窗口', 'RATE_LIMIT', 2, '60', 'SECOND', 'API', 1, 0, 'system', 'system', '接口限流默认时间窗口，单位为秒，本阶段只作为策略配置展示。'),
('SP-HIGH-RISK-HANDLE-SLA', '高风险处理时限', 'RISK', 3, '1', 'DAY', 'GLOBAL', 1, 0, 'system', 'system', '高风险登录、异常访问或关键安全事件建议在该时限内处理。'),
('SP-AUDIT-LOG-RETENTION', '审计日志保留天数', 'AUDIT', 1, '180', 'DAY', 'GLOBAL', 1, 0, 'system', 'system', 'IAM 接口访问日志、登录风险和安全策略操作记录建议保留天数。')
ON DUPLICATE KEY UPDATE
  `policy_name` = VALUES(`policy_name`),
  `policy_type` = VALUES(`policy_type`),
  `policy_level` = VALUES(`policy_level`),
  `policy_value` = VALUES(`policy_value`),
  `policy_unit` = VALUES(`policy_unit`),
  `effective_scope` = VALUES(`effective_scope`),
  `enabled` = VALUES(`enabled`),
  `status` = VALUES(`status`),
  `update_by` = VALUES(`update_by`),
  `remark` = VALUES(`remark`),
  `deleted` = 0;

SET NAMES utf8mb4;

USE enterprise_scaffold;

CREATE TABLE IF NOT EXISTS iam_login_risk (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  risk_code VARCHAR(64) NOT NULL COMMENT '风险编码',
  username VARCHAR(64) DEFAULT NULL COMMENT '登录用户名',
  client_ip VARCHAR(64) DEFAULT NULL COMMENT '客户端IP',
  risk_type VARCHAR(64) NOT NULL COMMENT '风险类型：LOGIN_FAILED登录失败，SHORT_TIME_FAILED短时间多次失败，ABNORMAL_IP异常IP',
  risk_level TINYINT NOT NULL DEFAULT 1 COMMENT '风险等级：1低风险，2中风险，3高风险',
  fail_count INT NOT NULL DEFAULT 0 COMMENT '失败次数',
  first_fail_time DATETIME DEFAULT NULL COMMENT '首次失败时间',
  last_fail_time DATETIME DEFAULT NULL COMMENT '最近失败时间',
  detect_time DATETIME NOT NULL COMMENT '检测时间',
  handle_status TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态：0未处理，1已确认，2已忽略',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '业务状态：0有效，1无效',
  create_by VARCHAR(64) DEFAULT 'system' COMMENT '创建人',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
  remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_risk_code (risk_code),
  KEY idx_username (username),
  KEY idx_client_ip (client_ip),
  KEY idx_risk_type (risk_type),
  KEY idx_risk_level (risk_level),
  KEY idx_handle_status (handle_status),
  KEY idx_detect_time (detect_time),
  KEY idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IAM异常登录风险表';

INSERT IGNORE INTO iam_login_risk
(risk_code, username, client_ip, risk_type, risk_level, fail_count, first_fail_time, last_fail_time, detect_time, handle_status, status, create_by, create_time, deleted, remark)
VALUES
('LR-DEMO-20260709-0001', 'admin', '127.0.0.1', 'LOGIN_FAILED', 1, 1, NOW() - INTERVAL 2 HOUR, NOW() - INTERVAL 2 HOUR, NOW(), 0, 0, 'system', NOW(), 0, '演示数据：单次登录失败'),
('LR-DEMO-20260709-0002', 'admin', '192.168.1.20', 'SHORT_TIME_FAILED', 3, 5, NOW() - INTERVAL 8 MINUTE, NOW() - INTERVAL 1 MINUTE, NOW(), 0, 0, 'system', NOW(), 0, '演示数据：短时间多次登录失败'),
('LR-DEMO-20260709-0003', 'test', '10.0.0.8', 'ABNORMAL_IP', 2, 4, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 30 MINUTE, NOW(), 0, 0, 'system', NOW(), 0, '演示数据：异常IP登录失败次数较多');

SELECT
  id,
  risk_code,
  username,
  client_ip,
  risk_type,
  risk_level,
  fail_count,
  detect_time
FROM iam_login_risk
ORDER BY id DESC
LIMIT 10;

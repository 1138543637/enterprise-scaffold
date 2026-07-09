SET NAMES utf8mb4;

USE enterprise_scaffold;

ALTER TABLE iam_login_risk
    MODIFY COLUMN handle_status tinyint NOT NULL DEFAULT 0 COMMENT '处理状态：0未处理，1已确认，2已忽略，3已关闭';

ALTER TABLE iam_login_risk
    ADD COLUMN handle_by varchar(64) DEFAULT NULL COMMENT '处理人' AFTER handle_status,
    ADD COLUMN handle_time datetime DEFAULT NULL COMMENT '处理时间' AFTER handle_by,
    ADD COLUMN handle_remark varchar(500) DEFAULT NULL COMMENT '处理备注' AFTER handle_time;

ALTER TABLE iam_login_risk
    ADD INDEX idx_iam_login_risk_handle_status_time (handle_status, handle_time);

SET NAMES utf8mb4;

USE enterprise_scaffold;

CREATE TABLE IF NOT EXISTS iam_permission_audit (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    audit_code VARCHAR(64) NOT NULL COMMENT '审计编码',
    audit_type VARCHAR(64) NOT NULL COMMENT '审计类型：USER_ROLE用户角色变更，ROLE_MENU角色菜单变更，USER_STATUS用户状态变更，MENU_PERMISSION菜单权限变更，DATA_SCOPE数据范围变更',
    target_type VARCHAR(64) NOT NULL COMMENT '审计对象类型：USER用户，ROLE角色，MENU菜单，API接口',
    target_id BIGINT NULL COMMENT '审计对象ID',
    target_name VARCHAR(128) NULL COMMENT '审计对象名称',
    change_action VARCHAR(64) NOT NULL COMMENT '变更动作：GRANT授权，REVOKE撤销，UPDATE修改，ENABLE启用，DISABLE停用',
    before_value TEXT NULL COMMENT '变更前内容',
    after_value TEXT NULL COMMENT '变更后内容',
    risk_level VARCHAR(32) NOT NULL DEFAULT 'LOW' COMMENT '风险等级：LOW低，MEDIUM中，HIGH高',
    review_status VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '复核状态：PENDING待复核，REVIEWED已复核，IGNORED已忽略',
    review_by VARCHAR(64) NULL COMMENT '复核人',
    review_time DATETIME NULL COMMENT '复核时间',
    request_ip VARCHAR(64) NULL COMMENT '请求IP',
    operator_name VARCHAR(64) NULL COMMENT '操作人',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '业务状态：0正常，1禁用',
    create_by VARCHAR(64) NULL DEFAULT 'system' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NULL COMMENT '更新人',
    update_time DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_iam_permission_audit_code (audit_code),
    KEY idx_iam_permission_audit_type (audit_type),
    KEY idx_iam_permission_target_type (target_type),
    KEY idx_iam_permission_action (change_action),
    KEY idx_iam_permission_risk_level (risk_level),
    KEY idx_iam_permission_review_status (review_status),
    KEY idx_iam_permission_operator (operator_name),
    KEY idx_iam_permission_deleted (deleted),
    KEY idx_iam_permission_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='IAM权限审计记录表';

INSERT IGNORE INTO iam_permission_audit
(audit_code, audit_type, target_type, target_id, target_name, change_action, before_value, after_value, risk_level, review_status, request_ip, operator_name, status, create_by, create_time, remark)
VALUES
('PA-USER-ROLE-001', 'USER_ROLE', 'USER', 1, 'admin', 'GRANT', 'roles=[普通用户]', 'roles=[普通用户,系统管理员]', 'HIGH', 'PENDING', '127.0.0.1', 'admin', 0, 'system', NOW(), '初始化权限审计样例：用户被授予管理员角色'),
('PA-USER-ROLE-002', 'USER_ROLE', 'USER', 2, 'demo', 'REVOKE', 'roles=[系统管理员,审计员]', 'roles=[审计员]', 'HIGH', 'PENDING', '127.0.0.1', 'admin', 0, 'system', NOW(), '初始化权限审计样例：用户管理员角色被撤销'),
('PA-ROLE-MENU-001', 'ROLE_MENU', 'ROLE', 1, '系统管理员', 'GRANT', 'menus=[首页]', 'menus=[首页,用户管理,角色管理,菜单管理]', 'MEDIUM', 'REVIEWED', '127.0.0.1', 'admin', 0, 'system', NOW(), '初始化权限审计样例：角色新增菜单权限'),
('PA-MENU-PERM-001', 'MENU_PERMISSION', 'MENU', 100, '权限审计', 'UPDATE', 'perms=iam:audit:view', 'perms=iam:audit:view,iam:audit:review', 'MEDIUM', 'PENDING', '127.0.0.1', 'admin', 0, 'system', NOW(), '初始化权限审计样例：菜单权限标识变化'),
('PA-USER-STATUS-001', 'USER_STATUS', 'USER', 3, 'risk_user', 'DISABLE', 'status=0', 'status=1', 'HIGH', 'PENDING', '127.0.0.1', 'admin', 0, 'system', NOW(), '初始化权限审计样例：高风险用户被停用'),
('PA-DATA-SCOPE-001', 'DATA_SCOPE', 'ROLE', 2, '数据审计员', 'UPDATE', 'scope=全部数据', 'scope=本部门数据', 'LOW', 'IGNORED', '127.0.0.1', 'admin', 0, 'system', NOW(), '初始化权限审计样例：数据范围变更');

USE enterprise_scaffold;

INSERT INTO sys_user
(dept_id, username, nickname, password_hash, user_type, email, phone, gender, status, create_by, remark)
VALUES
    (1, 'mine_operator', '矿山调度员', '{todo}generate-bcrypt-in-s0-05', 'SYSTEM', 'mine@example.com', '13800000001', 1, 0, 'system', 'S0-04测试用户：智能矿山方向'),
    (1, 'aiops_engineer', '运维工程师', '{todo}generate-bcrypt-in-s0-05', 'SYSTEM', 'aiops@example.com', '13800000002', 1, 0, 'system', 'S0-04测试用户：AIOps方向'),
    (1, 'risk_auditor', '风控审核员', '{todo}generate-bcrypt-in-s0-05', 'SYSTEM', 'risk@example.com', '13800000003', 2, 0, 'system', 'S0-04测试用户：银行风控方向')
    ON DUPLICATE KEY UPDATE
                         nickname = VALUES(nickname),
                         email = VALUES(email),
                         phone = VALUES(phone),
                         remark = VALUES(remark),
                         update_by = 'system',
                         update_time = CURRENT_TIMESTAMP;
USE enterprise_scaffold;

UPDATE sys_user
SET password_hash = '$2y$10$zx/XeLqcfldQ2CnKQpPNqu5g1fuQfwqZ4veKbdhPQ0OnYF2JSeXPG',
    update_by = 'system',
    update_time = CURRENT_TIMESTAMP,
    remark = 'S0-05 初始化登录密码，明文密码为 admin123'
WHERE username = 'admin';
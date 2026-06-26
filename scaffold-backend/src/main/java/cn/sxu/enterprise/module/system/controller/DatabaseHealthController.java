package cn.sxu.enterprise.module.system.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DatabaseHealthController {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseHealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/health/db")
    public ApiResult<Map<String, Object>> dbHealth() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

        Map<String, Object> data = new HashMap<>();
        data.put("database", "enterprise_scaffold");
        data.put("connected", result != null && result == 1);
        data.put("result", result);

        return ApiResult.success(data);
    }
}
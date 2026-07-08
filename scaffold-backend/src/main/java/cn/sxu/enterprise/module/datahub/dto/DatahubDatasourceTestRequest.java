package cn.sxu.enterprise.module.datahub.dto;

import jakarta.validation.constraints.NotBlank;

public class DatahubDatasourceTestRequest {

    @NotBlank(message = "数据源类型不能为空")
    private String datasourceType;

    @NotBlank(message = "JDBC连接地址不能为空")
    private String jdbcUrl;

    private String username;

    private String password;

    public String getDatasourceType() {
        return datasourceType;
    }

    public void setDatasourceType(String datasourceType) {
        this.datasourceType = datasourceType;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

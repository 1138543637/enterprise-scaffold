package cn.sxu.enterprise.module.datahub.vo;

import cn.sxu.enterprise.module.datahub.entity.DatahubDatasource;

import java.time.LocalDateTime;

public class DatahubDatasourcePageVO {

    private Long id;
    private String datasourceCode;
    private String datasourceName;
    private String datasourceType;
    private String jdbcUrl;
    private String host;
    private Integer port;
    private String databaseName;
    private String username;
    private String ownerName;
    private String envType;
    private Integer testStatus;
    private LocalDateTime lastTestTime;
    private Integer status;
    private LocalDateTime createTime;
    private String remark;

    public static DatahubDatasourcePageVO fromEntity(DatahubDatasource entity) {
        DatahubDatasourcePageVO vo = new DatahubDatasourcePageVO();
        vo.setId(entity.getId());
        vo.setDatasourceCode(entity.getDatasourceCode());
        vo.setDatasourceName(entity.getDatasourceName());
        vo.setDatasourceType(entity.getDatasourceType());
        vo.setJdbcUrl(entity.getJdbcUrl());
        vo.setHost(entity.getHost());
        vo.setPort(entity.getPort());
        vo.setDatabaseName(entity.getDatabaseName());
        vo.setUsername(entity.getUsername());
        vo.setOwnerName(entity.getOwnerName());
        vo.setEnvType(entity.getEnvType());
        vo.setTestStatus(entity.getTestStatus());
        vo.setLastTestTime(entity.getLastTestTime());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatasourceCode() {
        return datasourceCode;
    }

    public void setDatasourceCode(String datasourceCode) {
        this.datasourceCode = datasourceCode;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getEnvType() {
        return envType;
    }

    public void setEnvType(String envType) {
        this.envType = envType;
    }

    public Integer getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(Integer testStatus) {
        this.testStatus = testStatus;
    }

    public LocalDateTime getLastTestTime() {
        return lastTestTime;
    }

    public void setLastTestTime(LocalDateTime lastTestTime) {
        this.lastTestTime = lastTestTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

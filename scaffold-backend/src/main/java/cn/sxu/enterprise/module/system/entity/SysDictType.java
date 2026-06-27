package cn.sxu.enterprise.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("sys_dict_type")
public class SysDictType {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String dictName;
    private String dictType;
    private Integer status;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Integer deleted;
    private String remark;

    public Long getId() {
        return id;
    }

    public String getDictName() {
        return dictName;
    }

    public String getDictType() {
        return dictType;
    }

    public Integer getStatus() {
        return status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public String getRemark() {
        return remark;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
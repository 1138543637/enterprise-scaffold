package cn.sxu.enterprise.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("sys_dict_data")
public class SysDictData {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String dictType;
    private String dictLabel;
    private String dictValue;
    private Integer dictSort;
    private String cssClass;
    private String listClass;
    private String isDefault;
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

    public String getDictType() {
        return dictType;
    }

    public String getDictLabel() {
        return dictLabel;
    }

    public String getDictValue() {
        return dictValue;
    }

    public Integer getDictSort() {
        return dictSort;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String getListClass() {
        return listClass;
    }

    public String getIsDefault() {
        return isDefault;
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

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public void setDictSort(Integer dictSort) {
        this.dictSort = dictSort;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setListClass(String listClass) {
        this.listClass = listClass;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
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
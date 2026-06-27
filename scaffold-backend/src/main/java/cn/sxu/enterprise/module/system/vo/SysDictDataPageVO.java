package cn.sxu.enterprise.module.system.vo;

import cn.sxu.enterprise.module.system.entity.SysDictData;

import java.time.LocalDateTime;

public class SysDictDataPageVO {

    private Long id;
    private String dictType;
    private String dictLabel;
    private String dictValue;
    private Integer dictSort;
    private String cssClass;
    private String listClass;
    private String isDefault;
    private Integer status;
    private LocalDateTime createTime;
    private String remark;

    public static SysDictDataPageVO fromEntity(SysDictData entity) {
        SysDictDataPageVO vo = new SysDictDataPageVO();
        vo.setId(entity.getId());
        vo.setDictType(entity.getDictType());
        vo.setDictLabel(entity.getDictLabel());
        vo.setDictValue(entity.getDictValue());
        vo.setDictSort(entity.getDictSort());
        vo.setCssClass(entity.getCssClass());
        vo.setListClass(entity.getListClass());
        vo.setIsDefault(entity.getIsDefault());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

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

    public LocalDateTime getCreateTime() {
        return createTime;
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

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

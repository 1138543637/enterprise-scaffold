package cn.sxu.enterprise.module.system.vo;

import cn.sxu.enterprise.module.system.entity.SysDictData;

public class SysDictDataVO {

    private Long id;
    private String dictType;
    private String dictLabel;
    private String dictValue;
    private Integer dictSort;
    private String listClass;
    private String isDefault;

    public static SysDictDataVO fromEntity(SysDictData entity) {
        SysDictDataVO vo = new SysDictDataVO();
        vo.setId(entity.getId());
        vo.setDictType(entity.getDictType());
        vo.setDictLabel(entity.getDictLabel());
        vo.setDictValue(entity.getDictValue());
        vo.setDictSort(entity.getDictSort());
        vo.setListClass(entity.getListClass());
        vo.setIsDefault(entity.getIsDefault());
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

    public String getListClass() {
        return listClass;
    }

    public String getIsDefault() {
        return isDefault;
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

    public void setListClass(String listClass) {
        this.listClass = listClass;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}

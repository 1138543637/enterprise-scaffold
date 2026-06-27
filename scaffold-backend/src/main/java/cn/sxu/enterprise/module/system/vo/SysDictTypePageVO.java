package cn.sxu.enterprise.module.system.vo;

import cn.sxu.enterprise.module.system.entity.SysDictType;

import java.time.LocalDateTime;

public class SysDictTypePageVO {

    private Long id;
    private String dictName;
    private String dictType;
    private Integer status;
    private LocalDateTime createTime;
    private String remark;

    public static SysDictTypePageVO fromEntity(SysDictType entity) {
        SysDictTypePageVO vo = new SysDictTypePageVO();
        vo.setId(entity.getId());
        vo.setDictName(entity.getDictName());
        vo.setDictType(entity.getDictType());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

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

    public LocalDateTime getCreateTime() {
        return createTime;
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

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

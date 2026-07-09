package cn.sxu.enterprise.module.datahub.vo;

import cn.sxu.enterprise.module.datahub.entity.DatahubMaskRule;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DatahubMaskRulePageVO {

    private Long id;
    private String ruleCode;
    private String ruleName;
    private String sensitiveType;
    private String maskMethod;
    private Integer keepPrefix;
    private Integer keepSuffix;
    private String maskChar;
    private Integer status;
    private LocalDateTime createTime;
    private String remark;

    public static DatahubMaskRulePageVO fromEntity(DatahubMaskRule entity) {
        if (entity == null) {
            return null;
        }
        DatahubMaskRulePageVO vo = new DatahubMaskRulePageVO();
        vo.setId(entity.getId());
        vo.setRuleCode(entity.getRuleCode());
        vo.setRuleName(entity.getRuleName());
        vo.setSensitiveType(entity.getSensitiveType());
        vo.setMaskMethod(entity.getMaskMethod());
        vo.setKeepPrefix(entity.getKeepPrefix());
        vo.setKeepSuffix(entity.getKeepSuffix());
        vo.setMaskChar(entity.getMaskChar());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }
}

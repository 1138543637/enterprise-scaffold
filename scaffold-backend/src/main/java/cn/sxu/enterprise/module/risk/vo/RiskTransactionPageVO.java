package cn.sxu.enterprise.module.risk.vo;

import cn.sxu.enterprise.module.risk.entity.RiskTransaction;

public class RiskTransactionPageVO extends RiskTransactionVO {

    public static RiskTransactionPageVO fromEntity(RiskTransaction entity) {
        RiskTransactionPageVO vo = new RiskTransactionPageVO();
        fillFromEntity(vo, entity);
        return vo;
    }
}

package cn.sxu.enterprise.module.iam.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.iam.vo.IamLoginRiskPageQuery;
import cn.sxu.enterprise.module.iam.vo.IamLoginRiskPageVO;

import java.util.List;

/**
 * IAM 异常登录风险 Service。
 */
public interface IamLoginRiskService {

    PageResult<IamLoginRiskPageVO> pageLoginRisks(IamLoginRiskPageQuery query);

    List<IamLoginRiskPageVO> detectLoginRisks();
}

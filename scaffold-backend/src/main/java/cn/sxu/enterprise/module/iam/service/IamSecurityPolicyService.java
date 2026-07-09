package cn.sxu.enterprise.module.iam.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.iam.vo.IamSecurityPolicyPageQuery;
import cn.sxu.enterprise.module.iam.vo.IamSecurityPolicyPageVO;
import cn.sxu.enterprise.module.iam.vo.IamSecurityPolicyUpdateRequest;

public interface IamSecurityPolicyService {

    PageResult<IamSecurityPolicyPageVO> pageSecurityPolicies(IamSecurityPolicyPageQuery query);

    Boolean enablePolicy(Long id);

    Boolean disablePolicy(Long id);

    IamSecurityPolicyPageVO updatePolicy(Long id, IamSecurityPolicyUpdateRequest request);
}

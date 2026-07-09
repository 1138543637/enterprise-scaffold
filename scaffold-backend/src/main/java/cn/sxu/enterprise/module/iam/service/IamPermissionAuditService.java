package cn.sxu.enterprise.module.iam.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditPageQuery;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditPageVO;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditReviewRequest;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditSimulateRequest;

public interface IamPermissionAuditService {

    PageResult<IamPermissionAuditPageVO> pagePermissionAudits(IamPermissionAuditPageQuery query);

    IamPermissionAuditPageVO simulatePermissionAudit(IamPermissionAuditSimulateRequest request);

    IamPermissionAuditPageVO reviewPermissionAudit(Long id, IamPermissionAuditReviewRequest request);
}

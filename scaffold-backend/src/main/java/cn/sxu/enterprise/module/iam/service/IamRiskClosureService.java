package cn.sxu.enterprise.module.iam.service;

import cn.sxu.enterprise.module.iam.vo.IamLoginRiskHandleRequest;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditClosureRequest;
import cn.sxu.enterprise.module.iam.vo.IamRiskClosureSummaryVO;

/**
 * IAM 风险闭环处理服务。
 */
public interface IamRiskClosureService {

    IamRiskClosureSummaryVO getSummary();

    Boolean confirmLoginRisk(Long id, IamLoginRiskHandleRequest request);

    Boolean ignoreLoginRisk(Long id, IamLoginRiskHandleRequest request);

    Boolean closeLoginRisk(Long id, IamLoginRiskHandleRequest request);

    Boolean reviewPermissionAudit(Long id, IamPermissionAuditClosureRequest request);

    Boolean ignorePermissionAudit(Long id, IamPermissionAuditClosureRequest request);
}

package cn.sxu.enterprise.module.iam.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.iam.vo.IamAccessLogPageQuery;
import cn.sxu.enterprise.module.iam.vo.IamAccessLogPageVO;

/**
 * IAM 接口访问日志 Service。
 */
public interface IamAccessLogService {

    PageResult<IamAccessLogPageVO> pageAccessLogs(IamAccessLogPageQuery query);
}

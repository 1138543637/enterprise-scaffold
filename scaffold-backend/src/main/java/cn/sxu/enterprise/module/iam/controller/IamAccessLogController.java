package cn.sxu.enterprise.module.iam.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.iam.service.IamAccessLogService;
import cn.sxu.enterprise.module.iam.vo.IamAccessLogPageQuery;
import cn.sxu.enterprise.module.iam.vo.IamAccessLogPageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
 * IAM 接口访问日志 Controller。
 */
@RestController
@RequestMapping("/api/iam/access-logs")
public class IamAccessLogController {

    @Resource
    private IamAccessLogService iamAccessLogService;

    @OperLog(title = "统一身份认证与安全审计-接口访问日志", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<IamAccessLogPageVO>> page(IamAccessLogPageQuery query) {
        return ApiResult.success(iamAccessLogService.pageAccessLogs(query));
    }
}

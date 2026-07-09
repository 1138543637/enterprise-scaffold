package cn.sxu.enterprise.module.datahub.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.datahub.dto.DatahubApiPublishCreateRequest;
import cn.sxu.enterprise.module.datahub.service.DatahubApiPublishService;
import cn.sxu.enterprise.module.datahub.vo.DatahubApiPublishPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubApiPublishPageVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/datahub/api-publishes")
public class DatahubApiPublishController {

    private final DatahubApiPublishService datahubApiPublishService;

    @OperLog(title = "数据治理-API共享发布", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<DatahubApiPublishPageVO>> page(DatahubApiPublishPageQuery query) {
        return ApiResult.success(datahubApiPublishService.pageApiPublishes(query));
    }

    @OperLog(title = "数据治理-API共享发布", businessType = "从元数据表发布API")
    @PostMapping("/publish-from-table")
    public ApiResult<DatahubApiPublishPageVO> publishFromTable(@Valid @RequestBody DatahubApiPublishCreateRequest request) {
        return ApiResult.success(datahubApiPublishService.publishFromTable(request));
    }

    @OperLog(title = "数据治理-API共享发布", businessType = "API上线")
    @PostMapping("/{id}/online")
    public ApiResult<DatahubApiPublishPageVO> online(@PathVariable Long id) {
        return ApiResult.success(datahubApiPublishService.online(id));
    }

    @OperLog(title = "数据治理-API共享发布", businessType = "API下线")
    @PostMapping("/{id}/offline")
    public ApiResult<DatahubApiPublishPageVO> offline(@PathVariable Long id) {
        return ApiResult.success(datahubApiPublishService.offline(id));
    }
}

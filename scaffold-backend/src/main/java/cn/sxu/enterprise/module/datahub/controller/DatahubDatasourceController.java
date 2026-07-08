package cn.sxu.enterprise.module.datahub.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.datahub.dto.DatahubDatasourceTestRequest;
import cn.sxu.enterprise.module.datahub.service.DatahubDatasourceService;
import cn.sxu.enterprise.module.datahub.vo.DatahubDatasourcePageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubDatasourcePageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubDatasourceTestVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/datahub/datasources")
public class DatahubDatasourceController {

    private final DatahubDatasourceService datahubDatasourceService;

    public DatahubDatasourceController(DatahubDatasourceService datahubDatasourceService) {
        this.datahubDatasourceService = datahubDatasourceService;
    }

    @OperLog(title = "数据治理-数据源管理", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<DatahubDatasourcePageVO>> page(DatahubDatasourcePageQuery query) {
        return ApiResult.success(datahubDatasourceService.pageDatasources(query));
    }

    @OperLog(title = "数据治理-数据源管理", businessType = "连接测试")
    @PostMapping("/test-connection")
    public ApiResult<DatahubDatasourceTestVO> testConnection(@Valid @RequestBody DatahubDatasourceTestRequest request) {
        return ApiResult.success(datahubDatasourceService.testConnection(request));
    }
}

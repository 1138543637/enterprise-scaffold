package cn.sxu.enterprise.module.datahub.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.datahub.dto.DatahubMetadataCollectRequest;
import cn.sxu.enterprise.module.datahub.service.DatahubMetadataService;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataCollectLogPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataCollectLogPageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataCollectResultVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataColumnPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataColumnPageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataTablePageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataTablePageVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/datahub/metadata")
public class DatahubMetadataController {

    private final DatahubMetadataService datahubMetadataService;

    public DatahubMetadataController(DatahubMetadataService datahubMetadataService) {
        this.datahubMetadataService = datahubMetadataService;
    }

    @OperLog(title = "数据治理-元数据采集", businessType = "采集元数据")
    @PostMapping("/collect")
    public ApiResult<DatahubMetadataCollectResultVO> collect(@Valid @RequestBody DatahubMetadataCollectRequest request) {
        return ApiResult.success(datahubMetadataService.collect(request));
    }

    @OperLog(title = "数据治理-元数据表", businessType = "分页查询")
    @GetMapping("/tables/page")
    public ApiResult<PageResult<DatahubMetadataTablePageVO>> pageTables(
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") Long pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
            @RequestParam(value = "dataSourceId", required = false) Long dataSourceId,
            @RequestParam(value = "schemaName", required = false) String schemaName,
            @RequestParam(value = "tableName", required = false) String tableName,
            @RequestParam(value = "tableType", required = false) String tableType,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        DatahubMetadataTablePageQuery query = new DatahubMetadataTablePageQuery();
        query.pageNo = pageNo;
        query.pageSize = pageSize;
        query.dataSourceId = dataSourceId;
        query.schemaName = schemaName;
        query.tableName = tableName;
        query.tableType = tableType;
        query.status = status;
        return ApiResult.success(datahubMetadataService.pageTables(query));
    }

    @OperLog(title = "数据治理-元数据字段", businessType = "分页查询")
    @GetMapping("/columns/page")
    public ApiResult<PageResult<DatahubMetadataColumnPageVO>> pageColumns(
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") Long pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
            @RequestParam(value = "tableId", required = false) Long tableId,
            @RequestParam(value = "dataSourceId", required = false) Long dataSourceId,
            @RequestParam(value = "schemaName", required = false) String schemaName,
            @RequestParam(value = "tableName", required = false) String tableName,
            @RequestParam(value = "columnName", required = false) String columnName,
            @RequestParam(value = "dataType", required = false) String dataType,
            @RequestParam(value = "nullableFlag", required = false) Integer nullableFlag,
            @RequestParam(value = "primaryKeyFlag", required = false) Integer primaryKeyFlag,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        DatahubMetadataColumnPageQuery query = new DatahubMetadataColumnPageQuery();
        query.pageNo = pageNo;
        query.pageSize = pageSize;
        query.tableId = tableId;
        query.dataSourceId = dataSourceId;
        query.schemaName = schemaName;
        query.tableName = tableName;
        query.columnName = columnName;
        query.dataType = dataType;
        query.nullableFlag = nullableFlag;
        query.primaryKeyFlag = primaryKeyFlag;
        query.status = status;
        return ApiResult.success(datahubMetadataService.pageColumns(query));
    }

    @OperLog(title = "数据治理-元数据采集日志", businessType = "分页查询")
    @GetMapping("/collect-logs/page")
    public ApiResult<PageResult<DatahubMetadataCollectLogPageVO>> pageCollectLogs(
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") Long pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
            @RequestParam(value = "dataSourceId", required = false) Long dataSourceId,
            @RequestParam(value = "collectBatchNo", required = false) String collectBatchNo,
            @RequestParam(value = "collectStatus", required = false) Integer collectStatus,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        DatahubMetadataCollectLogPageQuery query = new DatahubMetadataCollectLogPageQuery();
        query.pageNo = pageNo;
        query.pageSize = pageSize;
        query.dataSourceId = dataSourceId;
        query.collectBatchNo = collectBatchNo;
        query.collectStatus = collectStatus;
        query.status = status;
        return ApiResult.success(datahubMetadataService.pageCollectLogs(query));
    }
}

package cn.sxu.enterprise.module.system.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.system.service.SysDictDataService;
import cn.sxu.enterprise.module.system.vo.SysDictDataPageQuery;
import cn.sxu.enterprise.module.system.vo.SysDictDataPageVO;
import cn.sxu.enterprise.module.system.vo.SysDictDataVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/system/dict-data")
public class SysDictDataController {

    private final SysDictDataService sysDictDataService;

    public SysDictDataController(SysDictDataService sysDictDataService) {
        this.sysDictDataService = sysDictDataService;
    }

    @OperLog(title = "字典数据", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<SysDictDataPageVO>> pageDictData(SysDictDataPageQuery query) {
        return ApiResult.success(sysDictDataService.pageDictData(query));
    }

    @OperLog(title = "字典数据", businessType = "按类型查询")
    @GetMapping("/type/{dictType}")
    public ApiResult<List<SysDictDataVO>> listByDictType(@PathVariable String dictType) {
        return ApiResult.success(sysDictDataService.listByDictType(dictType));
    }
}

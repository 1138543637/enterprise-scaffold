package cn.sxu.enterprise.module.system.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.system.service.SysDictTypeService;
import cn.sxu.enterprise.module.system.vo.SysDictTypePageQuery;
import cn.sxu.enterprise.module.system.vo.SysDictTypePageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/dict-types")
public class SysDictTypeController {

    private final SysDictTypeService sysDictTypeService;

    public SysDictTypeController(SysDictTypeService sysDictTypeService) {
        this.sysDictTypeService = sysDictTypeService;
    }

    @OperLog(title = "字典类型", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<SysDictTypePageVO>> pageDictTypes(SysDictTypePageQuery query) {
        return ApiResult.success(sysDictTypeService.pageDictTypes(query));
    }
}

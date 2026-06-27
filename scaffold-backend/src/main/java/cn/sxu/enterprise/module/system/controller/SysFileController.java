package cn.sxu.enterprise.module.system.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.system.service.SysFileService;
import cn.sxu.enterprise.module.system.vo.SysFilePageQuery;
import cn.sxu.enterprise.module.system.vo.SysFilePageVO;
import cn.sxu.enterprise.module.system.vo.SysFileUploadVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/system/files")
public class SysFileController {

    private final SysFileService sysFileService;

    public SysFileController(SysFileService sysFileService) {
        this.sysFileService = sysFileService;
    }

    @OperLog(title = "文件管理", businessType = "文件上传")
    @PostMapping("/upload")
    public ApiResult<SysFileUploadVO> upload(@RequestParam("file") MultipartFile file) {
        return ApiResult.success(sysFileService.upload(file));
    }

    @OperLog(title = "文件管理", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<SysFilePageVO>> pageFiles(SysFilePageQuery query) {
        return ApiResult.success(sysFileService.pageFiles(query));
    }
}

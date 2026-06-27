package cn.sxu.enterprise.module.system.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.system.vo.SysFilePageQuery;
import cn.sxu.enterprise.module.system.vo.SysFilePageVO;
import cn.sxu.enterprise.module.system.vo.SysFileUploadVO;
import org.springframework.web.multipart.MultipartFile;

public interface SysFileService {

    SysFileUploadVO upload(MultipartFile file);

    PageResult<SysFilePageVO> pageFiles(SysFilePageQuery query);
}

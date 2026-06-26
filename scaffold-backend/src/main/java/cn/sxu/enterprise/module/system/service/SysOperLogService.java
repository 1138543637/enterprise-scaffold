package cn.sxu.enterprise.module.system.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.system.entity.SysOperLog;
import cn.sxu.enterprise.module.system.vo.SysOperLogPageQuery;
import cn.sxu.enterprise.module.system.vo.SysOperLogPageVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysOperLogService extends IService<SysOperLog> {

    PageResult<SysOperLogPageVO> pageOperLogs(SysOperLogPageQuery query);
}
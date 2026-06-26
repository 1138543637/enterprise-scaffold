package cn.sxu.enterprise.module.system.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.system.entity.SysLoginLog;
import cn.sxu.enterprise.module.system.vo.SysLoginLogPageQuery;
import cn.sxu.enterprise.module.system.vo.SysLoginLogPageVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysLoginLogService extends IService<SysLoginLog> {

    void recordLogin(String username, Integer status, String msg);

    PageResult<SysLoginLogPageVO> pageLoginLogs(SysLoginLogPageQuery query);
}
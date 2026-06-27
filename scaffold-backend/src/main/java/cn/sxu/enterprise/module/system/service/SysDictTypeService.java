package cn.sxu.enterprise.module.system.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.system.vo.SysDictTypePageQuery;
import cn.sxu.enterprise.module.system.vo.SysDictTypePageVO;

public interface SysDictTypeService {

    PageResult<SysDictTypePageVO> pageDictTypes(SysDictTypePageQuery query);
}

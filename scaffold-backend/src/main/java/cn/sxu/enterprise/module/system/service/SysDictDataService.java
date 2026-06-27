package cn.sxu.enterprise.module.system.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.system.vo.SysDictDataPageQuery;
import cn.sxu.enterprise.module.system.vo.SysDictDataPageVO;
import cn.sxu.enterprise.module.system.vo.SysDictDataVO;

import java.util.List;

public interface SysDictDataService {

    PageResult<SysDictDataPageVO> pageDictData(SysDictDataPageQuery query);

    List<SysDictDataVO> listByDictType(String dictType);
}

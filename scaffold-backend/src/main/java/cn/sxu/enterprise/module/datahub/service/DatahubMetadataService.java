package cn.sxu.enterprise.module.datahub.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.datahub.dto.DatahubMetadataCollectRequest;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataCollectLogPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataCollectLogPageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataCollectResultVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataColumnPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataColumnPageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataTablePageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataTablePageVO;

public interface DatahubMetadataService {

    DatahubMetadataCollectResultVO collect(DatahubMetadataCollectRequest request);

    PageResult<DatahubMetadataTablePageVO> pageTables(DatahubMetadataTablePageQuery query);

    PageResult<DatahubMetadataColumnPageVO> pageColumns(DatahubMetadataColumnPageQuery query);

    PageResult<DatahubMetadataCollectLogPageVO> pageCollectLogs(DatahubMetadataCollectLogPageQuery query);
}

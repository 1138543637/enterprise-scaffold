package cn.sxu.enterprise.module.datahub.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.datahub.dto.DatahubDatasourceTestRequest;
import cn.sxu.enterprise.module.datahub.vo.DatahubDatasourcePageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubDatasourcePageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubDatasourceTestVO;

public interface DatahubDatasourceService {

    PageResult<DatahubDatasourcePageVO> pageDatasources(DatahubDatasourcePageQuery query);

    DatahubDatasourceTestVO testConnection(DatahubDatasourceTestRequest request);
}

package cn.sxu.enterprise.module.datahub.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.datahub.dto.DatahubApiPublishCreateRequest;
import cn.sxu.enterprise.module.datahub.vo.DatahubApiPublishPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubApiPublishPageVO;

public interface DatahubApiPublishService {

    PageResult<DatahubApiPublishPageVO> pageApiPublishes(DatahubApiPublishPageQuery query);

    DatahubApiPublishPageVO publishFromTable(DatahubApiPublishCreateRequest request);

    DatahubApiPublishPageVO online(Long id);

    DatahubApiPublishPageVO offline(Long id);
}

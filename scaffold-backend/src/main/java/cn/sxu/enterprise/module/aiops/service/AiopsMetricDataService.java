package cn.sxu.enterprise.module.aiops.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.aiops.dto.AiopsMetricDataSimulateRequest;
import cn.sxu.enterprise.module.aiops.vo.AiopsMetricDataPageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsMetricDataVO;

import java.util.List;

public interface AiopsMetricDataService {

    List<AiopsMetricDataVO> simulate(AiopsMetricDataSimulateRequest request);

    List<AiopsMetricDataVO> latest(AiopsMetricDataPageQuery query);

    PageResult<AiopsMetricDataVO> pageMetricData(AiopsMetricDataPageQuery query);
}

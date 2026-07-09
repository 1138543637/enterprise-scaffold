package cn.sxu.enterprise.module.datahub.service;

import cn.sxu.enterprise.module.datahub.vo.DatahubDashboardSummaryVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubDatasourceTypeStatVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityResultStatVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubRecentApiPublishVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubRecentQualityResultVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubSensitiveTypeStatVO;

import java.util.List;

public interface DatahubDashboardService {

    DatahubDashboardSummaryVO getSummary();

    List<DatahubDatasourceTypeStatVO> getDatasourceTypeStats();

    List<DatahubQualityResultStatVO> getQualityResultStats();

    List<DatahubSensitiveTypeStatVO> getSensitiveTypeStats();

    List<DatahubRecentQualityResultVO> getRecentQualityResults();

    List<DatahubRecentApiPublishVO> getRecentApis();
}

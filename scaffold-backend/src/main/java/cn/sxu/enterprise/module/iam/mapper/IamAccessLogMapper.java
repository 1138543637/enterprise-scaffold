package cn.sxu.enterprise.module.iam.mapper;

import cn.sxu.enterprise.module.iam.entity.IamAccessLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * IAM 接口访问日志 Mapper。
 */
@Mapper
public interface IamAccessLogMapper extends BaseMapper<IamAccessLog> {
}

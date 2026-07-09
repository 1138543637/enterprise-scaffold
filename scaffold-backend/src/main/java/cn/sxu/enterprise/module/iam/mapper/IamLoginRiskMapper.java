package cn.sxu.enterprise.module.iam.mapper;

import cn.sxu.enterprise.module.iam.entity.IamLoginRisk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * IAM 异常登录风险 Mapper。
 */
@Mapper
public interface IamLoginRiskMapper extends BaseMapper<IamLoginRisk> {
}

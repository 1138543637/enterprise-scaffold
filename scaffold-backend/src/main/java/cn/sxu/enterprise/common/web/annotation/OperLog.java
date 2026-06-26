package cn.sxu.enterprise.common.web.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperLog {

    /**
     * 模块标题，例如：用户管理、角色管理
     */
    String title();

    /**
     * 业务类型，例如：查询、新增、修改、删除、登录
     */
    String businessType() default "查询";
}
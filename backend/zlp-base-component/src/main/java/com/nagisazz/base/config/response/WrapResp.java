package com.nagisazz.base.config.response;

import java.lang.annotation.*;

/**
 * 包装返回Response注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WrapResp {


    /**
     * 框架自动封装response
     * 如不需要框架封装，请设置为false
     *
     * @return
     */
    boolean autoWrapResp() default true;


    /**
     * 框架自动封装分页，仅支持简单分页，复杂分页建议自己实现
     *
     * @return
     */
    boolean autoWrapPage() default false;

}

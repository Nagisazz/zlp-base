/*
 * @(#)SpringUtil.java 2018年9月9日下午1:10:39
 * message
 * Copyright 2018 Thuisoft, Inc. All rights reserved.
 * THUNISOFT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.nagisazz.base.util;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获取spring上下文对象以及spring-bean的工具类
 *
 * @version 1.0
 */
public class SpringUtil implements ApplicationContextAware {

    /**
     * 上下文对象实例
     */
    private static ApplicationContext applicationContext;

    private static void initApplicationContext(ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     *
     * @return result
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param applicationContext 上下文
     * @throws BeansException e
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtil.initApplicationContext(applicationContext);
    }

    /**
     * 通过name获取 Bean.
     *
     * @param name beanName
     * @return result
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz 类
     * @param <T>   类型
     * @return result
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过class获取Bean map.
     *
     * @param <T>   类型
     * @param clazz 类
     * @return result
     */
    public static <T> Map<String, T> getBeanMap(Class<T> clazz) {
        return getApplicationContext().getBeansOfType(clazz);
    }


    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name  beanName
     * @param clazz Clazz
     * @param <T>   泛型
     * @return result
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}

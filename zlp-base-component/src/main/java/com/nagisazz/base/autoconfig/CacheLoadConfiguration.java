package com.nagisazz.base.autoconfig;

import com.nagisazz.base.config.cache.CacheLoad;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@ConditionalOnClass({CacheLoad.class})
public class CacheLoadConfiguration {

    @Resource
    private List<CacheLoad> cacheLoadList;


}

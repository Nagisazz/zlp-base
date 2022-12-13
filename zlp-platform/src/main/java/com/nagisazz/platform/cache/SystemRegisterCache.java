package com.nagisazz.platform.cache;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.nagisazz.base.config.cache.InitHolder;
import com.nagisazz.base.dao.base.SystemRegisterMapper;
import com.nagisazz.base.entity.SystemRegister;
import com.nagisazz.base.enums.ValidEnum;

/**
 * SystemRegisterInit
 */
@Service
public class SystemRegisterCache implements InitHolder {

    private final Cache<String, SystemRegister> cache = CacheBuilder.newBuilder()
            //设置缓存的最大容量
            .maximumSize(300)
            //设置并发级别为8
            .concurrencyLevel(8)
            .build();

    @Resource
    private SystemRegisterMapper systemRegisterMapper;

    @Override
    public void init() {
        List<SystemRegister> systemRegisters = systemRegisterMapper.selectList(SystemRegister.builder().valid(ValidEnum.VALID.getCode()).build());
        systemRegisters.forEach(systemRegister -> {
            cache.put(systemRegister.getIdentifier(), systemRegister);
        });
    }

    public void put(String key, SystemRegister value) {

    }

    public SystemRegister get(String key) {
        return cache.getIfPresent(key);
    }

    public Map<Long, SystemRegister> getAll() {
        return null;
    }

    public void invalidate(Long key) {

    }

    public void clear() {

    }
}
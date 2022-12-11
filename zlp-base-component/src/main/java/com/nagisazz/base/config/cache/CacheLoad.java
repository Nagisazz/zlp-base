package com.nagisazz.base.config.cache;

import org.springframework.stereotype.Component;

import java.util.Map;

public interface CacheLoad {

    void put(String key, String value);

    String get(String key);

    Map<String, String> getAll();

    void invalidate(String key);

    void clear();
}

package com.nagisazz.base.config.cache;

public interface InitHolder {

    /**
     * 初始化，called by {@link InitHolderRunner#init()}
     */
    void init();
}

package com.nagisazz.base.config.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class InitHolderRunner {

    @Autowired
    private List<InitHolder> initHolderList;

    public void init() {
        for (InitHolder initHolder : initHolderList) {
            initHolder.init();
        }
    }
}

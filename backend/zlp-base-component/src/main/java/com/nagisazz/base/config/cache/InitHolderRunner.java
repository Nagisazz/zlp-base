package com.nagisazz.base.config.cache;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InitHolderRunner {

    @Autowired
    private List<InitHolder> initHolderList;

    public void init() {
        for (InitHolder initHolder : initHolderList) {
            initHolder.init();
        }
    }
}

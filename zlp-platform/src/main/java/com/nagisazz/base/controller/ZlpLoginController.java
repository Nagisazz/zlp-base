package com.nagisazz.base.controller;

import com.nagisazz.base.pojo.OperationResult;
import com.nagisazz.base.property.SystemProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("base")
public class ZlpLoginController {

    @Resource
    private SystemProperties systemProperties;

    @PostMapping("login")
    public OperationResult login(){
        return null;
    }
}

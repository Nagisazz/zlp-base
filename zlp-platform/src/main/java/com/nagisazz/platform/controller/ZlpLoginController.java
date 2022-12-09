package com.nagisazz.platform.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nagisazz.base.pojo.OperationResult;
import com.nagisazz.platform.pojo.dto.LoginParam;
import com.nagisazz.platform.service.ZlpLoginService;

/**
 * ZlpLoginController
 */
@RestController
public class ZlpLoginController {

    @Resource
    private ZlpLoginService zlpLoginService;

    @PostMapping("login")
    public OperationResult login(@RequestBody LoginParam loginParam){
        return zlpLoginService.login(loginParam);
    }

    @PostMapping("loginApp")
    public OperationResult loginApp(@RequestBody LoginParam loginParam){
        return zlpLoginService.loginApp(loginParam);
    }

    @PostMapping("bound")
    public OperationResult bound(@RequestBody LoginParam loginParam){
        return zlpLoginService.bound(loginParam);
    }
}

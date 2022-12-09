package com.nagisazz.platform.controller;

import com.nagisazz.base.pojo.OperationResult;
import com.nagisazz.platform.pojo.dto.LoginParam;
import com.nagisazz.platform.pojo.dto.UserParam;
import com.nagisazz.platform.service.ZlpLoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ZlpLoginController
 */
@RestController
@RequestMapping("sso")
public class ZlpLoginController {

    @Resource
    private ZlpLoginService zlpLoginService;

    /**
     * PC端登录
     *
     * @param loginParam
     * @return
     */
    @PostMapping("login")
    public OperationResult login(@RequestBody LoginParam loginParam) {
        return zlpLoginService.login(loginParam);
    }

    /**
     * WX小程序登录
     *
     * @param loginParam
     * @return
     */
    @PostMapping("loginApp")
    public OperationResult loginApp(@RequestBody LoginParam loginParam) {
        return zlpLoginService.loginApp(loginParam);
    }

    /**
     * WX小程序绑定
     *
     * @param loginParam
     * @return
     */
    @PostMapping("bound")
    public OperationResult bound(@RequestBody LoginParam loginParam) {
        return zlpLoginService.bound(loginParam);
    }

    /**
     * PC端用户注册
     *
     * @param userParam
     * @return
     */
    @PostMapping("register")
    public OperationResult register(@RequestBody UserParam userParam) {
        return zlpLoginService.register(userParam);
    }

    /**
     * WX小程序用户注册
     *
     * @param userParam
     * @return
     */
    @PostMapping("registerApp")
    public OperationResult registerApp(@RequestBody UserParam userParam) {
        return zlpLoginService.registerApp(userParam);
    }
}

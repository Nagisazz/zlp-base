package com.nagisazz.platform.controller;

import com.nagisazz.base.pojo.OperationResult;
import com.nagisazz.platform.pojo.dto.UserParam;
import com.nagisazz.platform.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("account")
public class AccountInfoController {

    @Resource
    private AccountService accountService;

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("info")
    public OperationResult info() {
        return accountService.info();
    }

    /**
     * 更新用户信息
     *
     * @param userParam
     * @return
     */
    @PostMapping("update")
    public OperationResult update(@RequestBody UserParam userParam) {
        return accountService.update(userParam);
    }

    /**
     * 更新用户密码
     *
     * @param userParam
     * @return
     */
    @PostMapping("updatePassword")
    public OperationResult updatePassword(@RequestBody UserParam userParam) {
        return accountService.updatePassword(userParam);
    }

    /**
     * 刷新Token
     *
     * @return
     */
    @GetMapping("refresh")
    public OperationResult refresh() {
        return accountService.refresh();
    }
}

package com.nagisazz.platform.controller;

import com.nagisazz.base.config.msg.MsgPushVo;
import com.nagisazz.base.pojo.OperationResult;
import com.nagisazz.platform.pojo.dto.PushPlusCallbackParam;
import com.nagisazz.platform.pojo.vo.PushPlusCallbackVo;
import com.nagisazz.platform.service.MsgService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MsgController
 */
@RestController
@RequestMapping("msg")
public class MsgController {

    @Resource
    private MsgService msgService;

    /**
     * 发送消息
     *
     * @param msgPushVo
     * @return
     */
    @PostMapping("send")
    public OperationResult send(@RequestBody MsgPushVo msgPushVo) {
        msgService.send(msgPushVo);
        return OperationResult.buildSuccessResult("发送成功");
    }

    @PostMapping("callback")
    public PushPlusCallbackVo callback(@RequestBody PushPlusCallbackParam callbackParam){
        msgService.callback(callbackParam);
        return PushPlusCallbackVo.builder().code(200).msg("success").build();
    }
}

package com.nagisazz.platform.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nagisazz.base.config.msg.MsgPushVo;
import com.nagisazz.base.dao.MsgSendLogExtendMapper;
import com.nagisazz.base.entity.MsgSendLog;
import com.nagisazz.base.enums.ResultEnum;
import com.nagisazz.base.property.MsgPushProperties;
import com.nagisazz.base.util.RestHelper;
import com.nagisazz.platform.pojo.dto.PushPlusCallbackParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * MsgService
 */
@Slf4j
@Service
public class MsgService {

    @Resource
    private MsgSendLogExtendMapper msgSendLogExtendMapper;

    @Resource
    private RestHelper restHelper;

    @Resource
    private MsgPushProperties msgPushProperties;

    /**
     * 发送消息
     *
     * @param msgPushVo
     */
    public void send(MsgPushVo msgPushVo) {
        msgPushVo.setToken(msgPushProperties.getToken());
        boolean flag = false;
        String serialNumber = null;
        // 发送消息
        try {
            final JSONObject res = JSONObject.parseObject(restHelper.post(msgPushProperties.getUrl(), msgPushVo));
            if (ResultEnum.SUCCESS.getCode().equals(res.getInteger("code"))) {
                serialNumber = res.getString("data");
                flag = true;
            }
        } catch (Exception e) {
            log.error("消息发送失败，msgPushVo：{}", JSON.toJSONString(msgPushVo), e);
        }
        LocalDateTime now = LocalDateTime.now();
        msgSendLogExtendMapper.insertSelective(MsgSendLog.builder()
                .serialNumber(serialNumber)
                .title(msgPushVo.getTitle())
                .content(msgPushVo.getContent())
                .receiveGroup(msgPushVo.getTopic())
                .receiveFriend(msgPushVo.getTo())
                .msgTemplate(msgPushVo.getTemplate())
                .msgChannel(msgPushVo.getChannel())
                .status(flag ? null : -1)
                .sendSystem(msgPushVo.getSystem())
                .msgType(msgPushVo.getMsgType())
                .createTime(now)
                .updateTime(now)
                .build());
    }

    /**
     * PushPlus回调
     *
     * @param callbackParam
     */
    public void callback(PushPlusCallbackParam callbackParam) {
        log.info("收到PushPlus回调：{}", JSONObject.toJSONString(callbackParam));
        try {
            msgSendLogExtendMapper.updateBySerialNumber(MsgSendLog.builder()
                    .serialNumber(callbackParam.getMessageInfo().getShortCode())
                    .failContent(callbackParam.getMessageInfo().getMessage())
                    .status(callbackParam.getMessageInfo().getSendStatus())
                    .updateTime(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.error("PushPlus回调失败：{}", JSONObject.toJSONString(callbackParam), e);
        }
    }
}

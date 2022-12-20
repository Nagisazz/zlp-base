package com.nagisazz.base.dao;

import com.nagisazz.base.dao.base.WechatUserMapper;
import com.nagisazz.base.entity.WechatUser;

public interface WechatUserExtendMapper extends WechatUserMapper {

    void updateByOpenId(WechatUser build);
}
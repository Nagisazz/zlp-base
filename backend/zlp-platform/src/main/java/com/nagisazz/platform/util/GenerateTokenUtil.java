package com.nagisazz.platform.util;

import com.alibaba.fastjson.JSON;
import com.nagisazz.base.config.constants.BaseConstant;
import com.nagisazz.base.entity.ZlpUser;
import com.nagisazz.base.util.JWTUtil;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class GenerateTokenUtil {

    public String genToken(ZlpUser zlpUser) {
        // key为userId,map包含user对象
        Map<String, String> map = new HashMap<>(4);
        map.put(BaseConstant.USER_STR, JSON.toJSONString(zlpUser));
        // 生成token
        return JWTUtil.getToken(map, String.valueOf(zlpUser.getId()));
    }

    public String genRefreshToken(ZlpUser zlpUser) {
        // key为userId,map包含user对象
        Map<String, String> map = new HashMap<>(4);
        map.put(BaseConstant.USER_STR, JSON.toJSONString(zlpUser));
        // 生成token
        return JWTUtil.getRefreshToken(map, String.valueOf(zlpUser.getId()));
    }
}

package com.nagisazz.base.util;

import com.alibaba.fastjson.JSON;
import com.nagisazz.base.config.constants.BaseConstant;
import com.nagisazz.base.entity.ZlpUser;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 获取token解析数据
 */
@UtilityClass
public class CommonWebUtil {

    /**
     * 获取用户主键
     *
     * @return
     */
    public static Long getUserId() {
        final String userIdStr = Objects.toString(getRequest().getAttribute(BaseConstant.USER_ID_STR), "");
        return StringUtils.isBlank(userIdStr) ? null : Long.valueOf(userIdStr);
    }

    /**
     * 获取用户登录实体
     *
     * @return
     */
    public static ZlpUser getUser() {
        final String userStr = (String) getRequest().getAttribute(BaseConstant.USER_STR);
        return JSON.parseObject(userStr, ZlpUser.class);
    }

    /**
     * 获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        return getRequest().getHeader(BaseConstant.TOKEN_HEAD);
    }

}

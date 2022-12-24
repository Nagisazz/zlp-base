package com.nagisazz.base.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nagisazz.base.config.constants.BaseConstant;
import com.nagisazz.base.config.exception.CustomException;
import com.nagisazz.base.enums.ResultEnum;
import com.nagisazz.base.property.SystemProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Component
public class JWTUtil {

    /**
     * 生成token
     *
     * @param map   传入payload
     * @param keyId 用户唯一标识
     * @return 返回token
     */
    public static String getToken(Map<String, String> map, String keyId) {
        JWTCreator.Builder builder = JWT.create().withKeyId(keyId);
        map.forEach(builder::withClaim);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, SystemProperties.jwtStatic.getTokenExpireTime());
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(SystemProperties.jwtStatic.getTokenSignature()));
    }

    /**
     * 生成refresh_token
     *
     * @param map   传入payload
     * @param keyId 用户唯一标识
     * @return 返回token
     */
    public static String getRefreshToken(Map<String, String> map, String keyId) {
        JWTCreator.Builder builder = JWT.create().withKeyId(keyId);
        map.forEach(builder::withClaim);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, SystemProperties.jwtStatic.getRefreshTokenExpireTime());
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(SystemProperties.jwtStatic.getRefreshTokenSignature()));
    }

    /**
     * 验证token
     *
     * @param token 前端传输的token
     */
    public static void verifyToken(String token) {
        JWT.require(Algorithm.HMAC256(SystemProperties.jwtStatic.getTokenSignature())).build().verify(token);
    }

    /**
     * 验证refresh_token
     *
     * @param token 前端传输的token
     */
    public static void verifyRefreshToken(String token) {
        JWT.require(Algorithm.HMAC256(SystemProperties.jwtStatic.getRefreshTokenSignature())).build().verify(token);
    }

    /**
     * 获取token数据
     *
     * @param token 前端传输的token
     * @return token数据
     */
    public static DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(SystemProperties.jwtStatic.getTokenSignature())).build().verify(token);
    }

    /**
     * 获取refresh_token数据
     *
     * @param token 前端传输的token
     * @return token数据
     */
    public static DecodedJWT decodeRefreshToken(String token) {
        return JWT.require(Algorithm.HMAC256(SystemProperties.jwtStatic.getRefreshTokenSignature())).build().verify(token);
    }

    /**
     * 解析token
     *
     * @param token jwt token
     * @return Claims中数据需均为 {@link String} 类型
     */
    public static Map<String, String> getMap(String token) {
        if (StringUtils.isBlank(token)) {
            throw new CustomException(ResultEnum.TOKEN_NOT_FOUND);
        }
        try {
            Map<String, String> map = new HashMap<>(32);
            // 校验token
            verifyToken(token);
            // 解密
            DecodedJWT decode = JWT.decode(token);
            String userId = decode.getKeyId();
            map.put(BaseConstant.USER_ID_STR, userId);
            final Map<String, Claim> claims = decode.getClaims();
            claims.forEach((k, v) -> map.put(k, v.asString()));
            return map;
        } catch (TokenExpiredException e) {
            throw new CustomException(ResultEnum.TOKEN_EXPIRED_FAIL, e);
        } catch (Exception e) {
            throw new CustomException(ResultEnum.TOKEN_DECODE_FAIL, e);
        }
    }

    /**
     * 解析refresh_token
     *
     * @param token jwt token
     * @return Claims中数据需均为 {@link String} 类型
     */
    public static Map<String, String> getRefreshMap(String token) {
        if (StringUtils.isBlank(token)) {
            throw new CustomException(ResultEnum.TOKEN_NOT_FOUND);
        }
        try {
            Map<String, String> map = new HashMap<>(32);
            // 校验token
            verifyRefreshToken(token);
            // 解密
            DecodedJWT decode = JWT.decode(token);
            String userId = decode.getKeyId();
            map.put(BaseConstant.USER_ID_STR, userId);
            final Map<String, Claim> claims = decode.getClaims();
            claims.forEach((k, v) -> map.put(k, v.asString()));
            return map;
        } catch (TokenExpiredException e) {
            throw new CustomException(ResultEnum.TOKEN_EXPIRED_FAIL, e);
        } catch (Exception e) {
            throw new CustomException(ResultEnum.TOKEN_DECODE_FAIL, e);
        }
    }
}

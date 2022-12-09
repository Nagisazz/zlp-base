package com.nagisazz.platform.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.nagisazz.base.dao.ZlpUserExtendMapper;
import com.nagisazz.base.dao.base.SystemRegisterMapper;
import com.nagisazz.base.dao.base.WechatUserMapper;
import com.nagisazz.base.entity.SystemRegister;
import com.nagisazz.base.entity.WechatUser;
import com.nagisazz.base.entity.ZlpUser;
import com.nagisazz.base.enums.ValidEnum;
import com.nagisazz.base.pojo.OperationResult;
import com.nagisazz.base.util.JWTUtil;
import com.nagisazz.base.util.RequestUtil;
import com.nagisazz.base.util.RestHelper;
import com.nagisazz.platform.enums.LoginResultEnum;
import com.nagisazz.platform.pojo.dto.LoginParam;
import com.nagisazz.platform.pojo.dto.WeChatLoginResult;
import com.nagisazz.platform.pojo.vo.UserInfoVo;

import lombok.extern.slf4j.Slf4j;

/**
 * ZlpLoginService
 */
@Slf4j
@Service
public class ZlpLoginService {

    @Value("${wx.loginurl}")
    private String loginUrl;

    @Resource
    private ZlpUserExtendMapper zlpUserMapper;

    @Resource
    private SystemRegisterMapper systemRegisterMapper;

    @Resource
    private WechatUserMapper wechatUserMapper;

    @Resource
    private RestHelper restHelper;

    /**
     * PC端登录
     *
     * @param loginParam
     * @return
     */
    public OperationResult login(LoginParam loginParam) {
        ZlpUser zlpUser = zlpUserMapper.selectOne(ZlpUser.builder()
                .loginid(loginParam.getLoginId())
                .password(loginParam.getPassword())
                .build());
        if (Objects.isNull(zlpUser)) {
            return OperationResult.buildFailureResult("用户名或者密码错误");
        } else if (ValidEnum.INVALID.getCode().equals(zlpUser.getValid())) {
            return OperationResult.buildFailureResult("用户被禁用，请联系微信号：NagisaZC解锁");
        }

        // 更新用户登录次数、最后登录时间、最后登录ip和最后登录系统
        zlpUserMapper.updateByPrimaryKeySelective(ZlpUser.builder()
                .id(zlpUser.getId())
                .loginNumber(zlpUser.getLoginNumber() + 1)
                .lastLoginTime(LocalDateTime.now())
                .lastIp(RequestUtil.getIp())
                .lastSystem(loginParam.getSystemId())
                .updateTime(LocalDateTime.now())
                .build());

        // 生成token
        String token = genToken(zlpUser);

        return buildUserVO(token, zlpUser);
    }

    /**
     * 小程序登录
     *
     * @param loginParam
     * @return
     */
    public OperationResult loginApp(LoginParam loginParam) {
        SystemRegister systemRegister = systemRegisterMapper.selectOne(SystemRegister.builder().identifier(loginParam.getSystemId()).build());
        if (StringUtils.isBlank(systemRegister.getWxAppid()) || StringUtils.isBlank(systemRegister.getWxSecret())) {
            return OperationResult.buildFailureResult(LoginResultEnum.NOT_REGISTER.getMessage());
        }

        final WeChatLoginResult weChatLoginResult = weChatLogin(systemRegister, loginParam);
        if (Objects.isNull(weChatLoginResult)) {
            return OperationResult.buildFailureResult(LoginResultEnum.WX_LOGIN_FAIL.getMessage());
        }

        final String openid = weChatLoginResult.getOpenid();
        final WechatUser wechatUser = wechatUserMapper.selectOne(WechatUser.builder().openid(openid).build());
        LocalDateTime now = LocalDateTime.now();
        // 微信数据为空，说明第一次登录，需插入微信用户数据，并返回前端用户未绑定
        if (Objects.isNull(wechatUser)) {
//            ZlpUser newUser = ZlpUser.builder()
//                    .registerSystem(loginParam.getSystemId())
//                    .lastIp(RequestUtil.getIp())
//                    .lastSystem(loginParam.getSystemId())
//                    .loginNumber(1)
//                    .valid(ValidEnum.VALID.getCode())
//                    .createTime(now)
//                    .updateTime(now)
//                    .lastLoginTime(now)
//                    .build();
//            zlpUserMapper.insertForId(newUser);
            wechatUserMapper.insertSelective(WechatUser.builder()
                    .openid(openid)
                    .unionid(weChatLoginResult.getUnionId())
                    .loginNumber(1)
                    .createTime(now)
                    .updateTime(now)
                    .build());
            return OperationResult.buildSuccessResult(LoginResultEnum.NOT_BOUND.getCode(), LoginResultEnum.NOT_BOUND.getMessage(), null);
        } else if (Objects.isNull(wechatUser.getUserId())) {
            // 返回前端用户未绑定
            return OperationResult.buildSuccessResult(LoginResultEnum.NOT_BOUND.getCode(), LoginResultEnum.NOT_BOUND.getMessage(), null);
        } else {
            ZlpUser zlpUser = zlpUserMapper.selectByPrimaryKey(wechatUser.getUserId());

            // 生成token
            String token = genToken(zlpUser);

            return buildUserVO(token, zlpUser);
        }
    }

    /**
     * 小程序绑定PC端账号
     *
     * @param loginParam
     * @return
     */
    public OperationResult bound(LoginParam loginParam) {
        SystemRegister systemRegister = systemRegisterMapper.selectOne(SystemRegister.builder().identifier(loginParam.getSystemId()).build());
        if (StringUtils.isBlank(systemRegister.getWxAppid()) || StringUtils.isBlank(systemRegister.getWxSecret())) {
            return OperationResult.buildFailureResult(LoginResultEnum.NOT_REGISTER.getMessage());
        }

        final WeChatLoginResult weChatLoginResult = weChatLogin(systemRegister, loginParam);
        if (Objects.isNull(weChatLoginResult)) {
            return OperationResult.buildFailureResult(LoginResultEnum.WX_LOGIN_FAIL.getMessage());
        }

        ZlpUser zlpUser = zlpUserMapper.selectOne(ZlpUser.builder()
                .loginid(loginParam.getLoginId())
                .password(loginParam.getPassword())
                .build());
        if (Objects.isNull(zlpUser)) {
            return OperationResult.buildFailureResult("用户名或者密码错误");
        } else if (ValidEnum.INVALID.getCode().equals(zlpUser.getValid())) {
            return OperationResult.buildFailureResult("用户被禁用，请联系微信号：NagisaZC解锁");
        }

        // todo 绑定用户
        return null;
    }

    /**
     * 微信登录
     *
     * @param loginParam 登陆参数
     * @return 登录返回值
     */
    private WeChatLoginResult weChatLogin(SystemRegister systemRegister, LoginParam loginParam) {
        final String code = loginParam.getCode();
        final String result = restHelper.get(String.format(loginUrl, systemRegister.getWxAppid(), systemRegister.getWxSecret(), code));
        if (Objects.isNull(result) || !JSONValidator.from(result).validate()) {
            return null;
        }
        final WeChatLoginResult weChatLoginResult = JSON.parseObject(result, WeChatLoginResult.class);
        final Integer errCode = weChatLoginResult.getErrCode();
        if (Objects.isNull(errCode) && Objects.nonNull(weChatLoginResult.getOpenid())) {
            return weChatLoginResult;
        }
        return null;
    }

    private String genToken(ZlpUser zlpUser) {
        // key为userId,map包含user对象
        Map<String, String> map = new HashMap<>(4);
        map.put("user", JSON.toJSONString(zlpUser));
        // 生成token
        return JWTUtil.getToken(map, String.valueOf(zlpUser.getId()));
    }

    private OperationResult buildUserVO(String token, ZlpUser zlpUser) {
        UserInfoVo userInfoVo = UserInfoVo.builder().build();
        BeanUtils.copyProperties(zlpUser, userInfoVo);
        userInfoVo.setToken(token);
        return OperationResult.buildSuccessResult(userInfoVo);
    }

}
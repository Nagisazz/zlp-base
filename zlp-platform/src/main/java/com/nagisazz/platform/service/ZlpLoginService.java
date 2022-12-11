package com.nagisazz.platform.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.nagisazz.base.dao.WechatUserExtendMapper;
import com.nagisazz.base.dao.ZlpUserExtendMapper;
import com.nagisazz.base.dao.base.SystemRegisterMapper;
import com.nagisazz.base.entity.SystemRegister;
import com.nagisazz.base.entity.WechatUser;
import com.nagisazz.base.entity.ZlpUser;
import com.nagisazz.base.enums.ValidEnum;
import com.nagisazz.base.pojo.OperationResult;
import com.nagisazz.base.util.RequestUtil;
import com.nagisazz.base.util.RestHelper;
import com.nagisazz.platform.enums.LoginResultEnum;
import com.nagisazz.platform.pojo.dto.LoginParam;
import com.nagisazz.platform.pojo.dto.UserParam;
import com.nagisazz.platform.pojo.dto.WeChatLoginResult;
import com.nagisazz.platform.pojo.vo.UserInfoVo;
import com.nagisazz.platform.util.GenerateTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ZlpLoginService
 */
@Slf4j
@Service
public class ZlpLoginService {

    @Value("${wx.loginurl}")
    private String loginUrl;

    @Resource
    private ZlpUserExtendMapper zlpUserExtendMapper;

    @Resource
    private SystemRegisterMapper systemRegisterMapper;

    @Resource
    private WechatUserExtendMapper wechatUserExtendMapper;

    @Resource
    private RestHelper restHelper;

    /**
     * PC端登录
     *
     * @param loginParam
     * @return
     */
    public OperationResult login(LoginParam loginParam) {
        Object res = judgeUser(loginParam.getLoginId(), loginParam.getPassword());
        if (res instanceof OperationResult) {
            return (OperationResult) res;
        } else {
            ZlpUser zlpUser = (ZlpUser) res;
            // 更新用户登录次数、最后登录时间、最后登录ip和最后登录系统
            zlpUserExtendMapper.updateByPrimaryKeySelective(ZlpUser.builder()
                    .id(zlpUser.getId())
                    .loginNumber(zlpUser.getLoginNumber() + 1)
                    .lastLoginTime(LocalDateTime.now())
                    .lastIp(RequestUtil.getIp())
                    .lastSystem(loginParam.getSystemId())
                    .updateTime(LocalDateTime.now())
                    .build());

            // 生成token并返回
            return buildUserRes(zlpUser);
        }
    }

    /**
     * WX小程序登录
     *
     * @param loginParam
     * @return
     */
    public OperationResult loginApp(LoginParam loginParam) {
        final WeChatLoginResult weChatLoginResult = weChatLogin(loginParam.getSystemId(), loginParam.getCode());
        if (Objects.isNull(weChatLoginResult)) {
            return OperationResult.buildFailureResult(LoginResultEnum.WX_LOGIN_FAIL.getMessage());
        }

        final String openid = weChatLoginResult.getOpenid();
        final WechatUser wechatUser = wechatUserExtendMapper.selectOne(WechatUser.builder().openid(openid).build());
        LocalDateTime now = LocalDateTime.now();
        // 微信数据为空，说明第一次登录，需插入微信用户数据，并返回前端用户未绑定
        if (Objects.isNull(wechatUser)) {
            wechatUserExtendMapper.insertSelective(WechatUser.builder()
                    .openid(openid)
                    .unionid(weChatLoginResult.getUnionId())
                    .loginNumber(1)
                    .createTime(now)
                    .updateTime(now)
                    .build());
            return OperationResult.buildSuccessResult(LoginResultEnum.NOT_BOUND.getCode(), LoginResultEnum.NOT_BOUND.getMessage(), null);
        } else if (Objects.isNull(wechatUser.getUserId())) {
            // 返回前端用户未绑定
            wechatUserExtendMapper.updateByPrimaryKey(WechatUser.builder()
                    .loginNumber(wechatUser.getLoginNumber() + 1)
                    .updateTime(now)
                    .build());
            return OperationResult.buildSuccessResult(LoginResultEnum.NOT_BOUND.getCode(), LoginResultEnum.NOT_BOUND.getMessage(), null);
        } else {
            ZlpUser zlpUser = zlpUserExtendMapper.selectByPrimaryKey(wechatUser.getUserId());
            // 生成token并返回
            return buildUserRes(zlpUser);
        }
    }

    /**
     * WX小程序绑定PC端账号
     *
     * @param loginParam
     * @return
     */
    public OperationResult bound(LoginParam loginParam) {
        final WeChatLoginResult weChatLoginResult = weChatLogin(loginParam.getSystemId(), loginParam.getCode());
        if (Objects.isNull(weChatLoginResult)) {
            return OperationResult.buildFailureResult(LoginResultEnum.WX_LOGIN_FAIL.getMessage());
        }

        Object res = judgeUser(loginParam.getLoginId(), loginParam.getPassword());
        if (res instanceof OperationResult) {
            return (OperationResult) res;
        } else {
            ZlpUser zlpUser = (ZlpUser) res;
            LocalDateTime now = LocalDateTime.now();
            // 绑定用户
            wechatUserExtendMapper.updateByOpenId(WechatUser.builder()
                    .openid(weChatLoginResult.getOpenid())
                    .userId(zlpUser.getId())
                    .updateTime(now)
                    .build());
            // 更新user
            zlpUser.setLastIp(RequestUtil.getIp());
            zlpUser.setLastLoginTime(now);
            zlpUser.setLastSystem(loginParam.getSystemId());
            zlpUser.setUpdateTime(now);
            zlpUserExtendMapper.updateByPrimaryKeySelective(zlpUser);
            // 生成token并返回
            return buildUserRes(zlpUser);
        }
    }

    /**
     * PC端用户注册
     *
     * @param userParam
     * @return
     */
    public OperationResult register(UserParam userParam) {
        if (!Objects.isNull(zlpUserExtendMapper.selectOne(ZlpUser.builder().loginId(userParam.getLoginId()).build()))) {
            return OperationResult.buildFailureResult("该账号已被注册");
        }
        // 注册用户
        final ZlpUser zlpUser = registerUser(userParam);
        // 生成token并返回
        return buildUserRes(zlpUser);
    }

    /**
     * WX小程序用户注册
     *
     * @param userParam
     * @return
     */
    public OperationResult registerApp(UserParam userParam) {
        if (!Objects.isNull(zlpUserExtendMapper.selectOne(ZlpUser.builder().loginId(userParam.getLoginId()).build()))) {
            return OperationResult.buildFailureResult("该账号已被注册");
        }
        // 微信登录
        final WeChatLoginResult weChatLoginResult = weChatLogin(userParam.getSystemId(), userParam.getCode());
        if (Objects.isNull(weChatLoginResult)) {
            return OperationResult.buildFailureResult(LoginResultEnum.WX_LOGIN_FAIL.getMessage());
        }
        // 注册用户
        final ZlpUser zlpUser = registerUser(userParam);
        // 绑定用户
        wechatUserExtendMapper.updateByOpenId(WechatUser.builder()
                .openid(weChatLoginResult.getOpenid())
                .userId(zlpUser.getId())
                .updateTime(LocalDateTime.now())
                .build());
        // 生成token并返回
        return buildUserRes(zlpUser);
    }

    private ZlpUser registerUser(UserParam userParam) {
        LocalDateTime now = LocalDateTime.now();

        ZlpUser zlpUser = ZlpUser.builder().build();
        BeanUtils.copyProperties(userParam, zlpUser);
        zlpUser.setLastIp(RequestUtil.getIp());
        zlpUser.setLastLoginTime(now);
        zlpUser.setLastSystem(userParam.getSystemId());
        zlpUser.setRegisterSystem(userParam.getSystemId());
        zlpUser.setLoginNumber(1);
        zlpUser.setValid(ValidEnum.VALID.getCode());
        zlpUser.setCreateTime(now);
        zlpUser.setUpdateTime(now);

        zlpUserExtendMapper.insertForId(zlpUser);
        return zlpUser;
    }

    private Object judgeUser(String loginId, String password) {
        ZlpUser zlpUser = zlpUserExtendMapper.selectOne(ZlpUser.builder()
                .loginId(loginId)
                .password(password)
                .build());
        if (Objects.isNull(zlpUser)) {
            return OperationResult.buildFailureResult("用户名或者密码错误");
        } else if (ValidEnum.INVALID.getCode().equals(zlpUser.getValid())) {
            return OperationResult.buildFailureResult("用户被禁用，请联系微信号：NagisaZC解锁");
        }
        return zlpUser;
    }

    private WeChatLoginResult weChatLogin(String systemId, String code) {
        SystemRegister systemRegister = systemRegisterMapper.selectOne(SystemRegister.builder().identifier(systemId).build());
        if (StringUtils.isBlank(systemRegister.getWxAppid()) || StringUtils.isBlank(systemRegister.getWxSecret())) {
            return null;
        }

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

    private OperationResult buildUserRes(ZlpUser zlpUser) {
        // 生成token
        String token = GenerateTokenUtil.genToken(zlpUser);
        String refreshToken = GenerateTokenUtil.genRefreshToken(zlpUser);
        // 封装userinfo
        UserInfoVo userInfoVo = UserInfoVo.builder().build();
        BeanUtils.copyProperties(zlpUser, userInfoVo);
        userInfoVo.setToken(token);
        userInfoVo.setRefreshToken(refreshToken);
        return OperationResult.buildSuccessResult(userInfoVo);
    }
}
package com.nagisazz.platform.service;

import com.nagisazz.base.dao.ZlpUserExtendMapper;
import com.nagisazz.base.entity.ZlpUser;
import com.nagisazz.base.pojo.OperationResult;
import com.nagisazz.base.util.CommonWebUtil;
import com.nagisazz.base.util.RequestUtil;
import com.nagisazz.platform.pojo.dto.UserParam;
import com.nagisazz.platform.pojo.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service
public class AccountService {

    @Resource
    private ZlpUserExtendMapper zlpUserExtendMapper;

    /**
     * 获取用户信息
     *
     * @return
     */
    public OperationResult info() {
        // 封装userinfo
        UserInfoVo userInfoVo = UserInfoVo.builder().build();
        BeanUtils.copyProperties(CommonWebUtil.getUser(), userInfoVo);
        return OperationResult.buildSuccessResult(userInfoVo);
    }

    /**
     * 用户更新
     *
     * @param userParam
     * @return
     */
    public OperationResult update(UserParam userParam) {
        ZlpUser zlpUser = ZlpUser.builder().build();
        BeanUtils.copyProperties(userParam, zlpUser);
        LocalDateTime now = LocalDateTime.now();
        zlpUser.setId(CommonWebUtil.getUserId());
        zlpUser.setLastIp(RequestUtil.getIp());
        zlpUser.setLastLoginTime(now);
        zlpUser.setLastSystem(userParam.getSystemId());
        zlpUser.setUpdateTime(now);
        zlpUserExtendMapper.updateByPrimaryKeySelective(zlpUser);
        // 封装userinfo
        UserInfoVo userInfoVo = UserInfoVo.builder().build();
        BeanUtils.copyProperties(CommonWebUtil.getUser(), userInfoVo);
        return OperationResult.buildSuccessResult(userInfoVo);
    }

}

package com.nagisazz.platform.service;

import com.nagisazz.base.dao.ZlpUserExtendMapper;
import com.nagisazz.base.entity.ZlpUser;
import com.nagisazz.base.pojo.OperationResult;
import com.nagisazz.base.util.CommonWebUtil;
import com.nagisazz.base.util.RequestUtil;
import com.nagisazz.platform.pojo.dto.UserParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service
public class AccountService {

    @Resource
    private ZlpUserExtendMapper zlpUserMapper;

    /**
     * 获取用户信息
     *
     * @return
     */
    public OperationResult info() {
        return OperationResult.buildSuccessResult(CommonWebUtil.getUser());
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
        zlpUserMapper.updateByPrimaryKeySelective(zlpUser);
        return OperationResult.buildSuccessResult(zlpUser);
    }

}

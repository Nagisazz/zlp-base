package com.nagisazz.base.dao;

import com.nagisazz.base.dao.base.ZlpUserMapper;
import com.nagisazz.base.entity.ZlpUser;

/**
 * ZlpUserExtendMapper
 */
public interface ZlpUserExtendMapper extends ZlpUserMapper {

    void insertForId(ZlpUser zlpUser);
}
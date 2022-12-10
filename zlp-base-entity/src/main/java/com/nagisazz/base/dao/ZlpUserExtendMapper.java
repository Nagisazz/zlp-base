package com.nagisazz.base.dao;

import com.nagisazz.base.dao.base.ZlpUserMapper;
import com.nagisazz.base.entity.ZlpUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * ZlpUserExtendMapper
 */
public interface ZlpUserExtendMapper extends ZlpUserMapper {

    void insertForId(ZlpUser zlpUser);
}
package com.nagisazz.base.dao.base;

import com.nagisazz.base.entity.WechatUser;
import java.util.List;

public interface WechatUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WechatUser wechatuser);

    int insertSelective(WechatUser wechatuser);

    WechatUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WechatUser wechatuser);

    int updateByPrimaryKey(WechatUser wechatuser);

    WechatUser selectOne(WechatUser wechatuser);

    List<WechatUser> selectList(WechatUser wechatuser);

    int deleteSelective(WechatUser wechatuser);

    int batchUpdate(List<WechatUser> wechatuserList);

    int batchUpdateSelective(List<WechatUser> wechatuserList);

    int batchInsert(List<WechatUser> wechatuserList);

    int batchDelete(List<Long> wechatuserList);

    List<WechatUser> batchSelect(List<Long> wechatuserList);
}
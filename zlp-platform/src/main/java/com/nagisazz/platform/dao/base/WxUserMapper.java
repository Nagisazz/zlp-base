package com.nagisazz.platform.dao.base;

import com.nagisazz.platform.entity.WxUser;
import java.util.List;

public interface WxUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WxUser wxuser);

    int insertSelective(WxUser wxuser);

    WxUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WxUser wxuser);

    int updateByPrimaryKey(WxUser wxuser);

    WxUser selectOne(WxUser wxuser);

    List<WxUser> selectList(WxUser wxuser);

    int deleteSelective(WxUser wxuser);

    int batchUpdate(List<WxUser> wxuserList);

    int batchUpdateSelective(List<WxUser> wxuserList);

    int batchInsert(List<WxUser> wxuserList);

    int batchDelete(List<Long> wxuserList);

    List<WxUser> batchSelect(List<Long> wxuserList);
}
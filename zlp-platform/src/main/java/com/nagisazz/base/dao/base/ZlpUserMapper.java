package com.nagisazz.base.dao.base;

import com.nagisazz.base.entity.ZlpUser;
import java.util.List;

public interface ZlpUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ZlpUser zlpuser);

    int insertSelective(ZlpUser zlpuser);

    ZlpUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ZlpUser zlpuser);

    int updateByPrimaryKey(ZlpUser zlpuser);

    ZlpUser selectOne(ZlpUser zlpuser);

    List<ZlpUser> selectList(ZlpUser zlpuser);

    int deleteSelective(ZlpUser zlpuser);

    int batchUpdate(List<ZlpUser> zlpuserList);

    int batchUpdateSelective(List<ZlpUser> zlpuserList);

    int batchInsert(List<ZlpUser> zlpuserList);

    int batchDelete(List<Long> zlpuserList);

    List<ZlpUser> batchSelect(List<Long> zlpuserList);
}
package com.nagisazz.base.dao.base;

import com.nagisazz.base.entity.SystemRegister;
import java.util.List;

public interface SystemRegisterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SystemRegister systemregister);

    int insertSelective(SystemRegister systemregister);

    SystemRegister selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SystemRegister systemregister);

    int updateByPrimaryKey(SystemRegister systemregister);

    SystemRegister selectOne(SystemRegister systemregister);

    List<SystemRegister> selectList(SystemRegister systemregister);

    int deleteSelective(SystemRegister systemregister);

    int batchUpdate(List<SystemRegister> systemregisterList);

    int batchUpdateSelective(List<SystemRegister> systemregisterList);

    int batchInsert(List<SystemRegister> systemregisterList);

    int batchDelete(List<Long> systemregisterList);

    List<SystemRegister> batchSelect(List<Long> systemregisterList);
}
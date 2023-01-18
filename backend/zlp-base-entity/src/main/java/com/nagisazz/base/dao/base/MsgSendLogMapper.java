package com.nagisazz.base.dao.base;

import com.nagisazz.base.entity.MsgSendLog;
import java.util.List;

public interface MsgSendLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MsgSendLog msgsendlog);

    int insertSelective(MsgSendLog msgsendlog);

    MsgSendLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MsgSendLog msgsendlog);

    int updateByPrimaryKeyWithBLOBs(MsgSendLog record);

    int updateByPrimaryKey(MsgSendLog msgsendlog);

    MsgSendLog selectOne(MsgSendLog msgsendlog);

    List<MsgSendLog> selectList(MsgSendLog msgsendlog);

    int deleteSelective(MsgSendLog msgsendlog);

    int batchUpdate(List<MsgSendLog> msgsendlogList);

    int batchUpdateSelective(List<MsgSendLog> msgsendlogList);

    int batchInsert(List<MsgSendLog> msgsendlogList);

    int batchDelete(List<Long> msgsendlogList);

    List<MsgSendLog> batchSelect(List<Long> msgsendlogList);
}
package com.nagisazz.base.dao;

import com.nagisazz.base.dao.base.MsgSendLogMapper;
import com.nagisazz.base.entity.MsgSendLog;

public interface MsgSendLogExtendMapper extends MsgSendLogMapper {

    void updateBySerialNumber(MsgSendLog msgsendlog);

}
package com.nagisazz.base.dao.base;

import com.nagisazz.base.entity.LogRecord;
import java.util.List;

public interface LogRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LogRecord logrecord);

    int insertSelective(LogRecord logrecord);

    LogRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LogRecord logrecord);

    int updateByPrimaryKeyWithBLOBs(LogRecord record);

    int updateByPrimaryKey(LogRecord logrecord);

    LogRecord selectOne(LogRecord logrecord);

    List<LogRecord> selectList(LogRecord logrecord);

    int deleteSelective(LogRecord logrecord);

    int batchUpdate(List<LogRecord> logrecordList);

    int batchUpdateSelective(List<LogRecord> logrecordList);

    int batchInsert(List<LogRecord> logrecordList);

    int batchDelete(List<Long> logrecordList);

    List<LogRecord> batchSelect(List<Long> logrecordList);
}
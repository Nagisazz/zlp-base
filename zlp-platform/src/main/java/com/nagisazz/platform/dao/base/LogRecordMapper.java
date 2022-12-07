package com.nagisazz.platform.dao.base;

import com.nagisazz.platform.entity.LogRecord;
import java.util.List;

public interface LogRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LogRecord logrecord);

    int insertSelective(LogRecord logrecord);

    LogRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LogRecord logrecord);

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
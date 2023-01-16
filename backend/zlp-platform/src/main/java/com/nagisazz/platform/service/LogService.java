package com.nagisazz.platform.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.nagisazz.base.dao.base.LogRecordMapper;
import com.nagisazz.base.entity.LogRecord;

import lombok.extern.slf4j.Slf4j;

/**
 * LogService
 */
@Slf4j
@Service
public class LogService {

    @Resource
    private LogRecordMapper logRecordMapper;

    /**
     * 存储日志
     *
     * @param recordList
     */
    public void record(List<LogRecord> recordList) {
        logRecordMapper.batchInsert(recordList);
    }
}
package com.nagisazz.platform.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.nagisazz.base.entity.LogRecord;
import com.nagisazz.base.pojo.OperationResult;
import com.nagisazz.platform.service.LogService;

/**
 * LogController
 */
@RestController
@RequestMapping("log")
public class LogController {

    @Resource
    private LogService logService;

    /**
     * 日志存储
     * @param recordList
     * @return
     */
    @PostMapping("record")
    public OperationResult record(@RequestBody List<LogRecord> recordList) {
        Preconditions.checkArgument(!CollectionUtils.isEmpty(recordList), "参数为空");
        logService.record(recordList);
        return OperationResult.buildSuccessResult("存储日志成功");
    }
}
package com.nagisazz.base.config.log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.nagisazz.base.config.constants.BaseConstant;
import com.nagisazz.base.entity.LogRecord;
import com.nagisazz.base.enums.ResultEnum;
import com.nagisazz.base.pojo.OperationResult;
import com.nagisazz.base.property.ZlpProperties;
import com.nagisazz.base.util.RestHelper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * AsyncSendLogHandler
 */
@Slf4j
@RequiredArgsConstructor
public class AsyncSendLogHandler {

    private final LogQueue logQueue;

    private final RestHelper restHelper;

    private final ZlpProperties zlpProperties;

    // 向平台发送保存日志请求的线程池
    private static final ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1);

    // 向平台发送保存日志请求的持续失败次数
    public static int failedCount = 0;

    public void init() {
        scheduledPool.scheduleAtFixedRate(new AsyncSendTask(), 0, zlpProperties.getLog().getSendInterval(), TimeUnit.SECONDS);
    }

    class AsyncSendTask implements Runnable {

        private List<LogRecord> logRecords = new ArrayList<LogRecord>();

        @Override
        public void run() {
            try {
                //获取日志
                logRecords = getElementFromQueue();
                if (CollectionUtils.isEmpty(logRecords)) {
                    return;
                }
                // 发送
                List<LogRecord> resultLogs = sendLogs(logRecords);
                logRecords.clear();
                // 全部发送成功
                if (CollectionUtils.isEmpty(resultLogs)) {
                    failedCount = 0;
                    return;
                }
                // 没有全部发送成功
                putLogs2Queue(resultLogs);
                failedCount++;
                if (failedCount >= 3) {//降低执行频率，减少错误日志打印
                    log.warn("向平台发送日志持续失败{}次，{}毫秒后进行下次发送尝试", failedCount, zlpProperties.getLog().getDelayInterval());
                    Thread.sleep(zlpProperties.getLog().getDelayInterval());
                }
            } catch (Exception e) {
                log.error("日志发送出现异常", e);
                if (!CollectionUtils.isEmpty(logRecords)) {
                    logRecords.clear();
                }
            }
        }

        public AsyncSendTask() {
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    log.info("JVM关闭时执行");
                    if (!CollectionUtils.isEmpty(logRecords)) {
                        log.info("jvm关闭时，开始发送线程中的集合，一共{}条数据", logRecords.size());
                        sendLogs(logRecords);
                    }
                }
            }));
        }
    }

    private List<LogRecord> getElementFromQueue() {
        List<LogRecord> logRecords = null;
        try {
            logRecords = logQueue.take();
        } catch (InterruptedException e) {
            log.error("从队列中获取日志出错", e);
            Thread.currentThread().interrupt();
        }
        return logRecords;
    }

    private void putLogs2Queue(List<LogRecord> auditLogs) {
        boolean success = logQueue.offer(auditLogs);
        if (!success) {
            log.error("队列无法接收这批日志，日志大小{}", auditLogs.size());
        }
    }

    /**
     * 将日志发送到平台
     *
     * @param records
     * @return
     */
    public List<LogRecord> sendLogs(List<LogRecord> records) {
        List<LogRecord> resultList = Lists.newArrayList();
        try {
            String res = restHelper.post(zlpProperties.getSystem().getPlatformUrl() + BaseConstant.LOG_RECORD_URL, records);
            OperationResult operationResult = JSON.parseObject(res, OperationResult.class);
            if (!ResultEnum.SUCCESS.getCode().equals(operationResult.getStatus())) {
                resultList.addAll(records);
            }
        } catch (Exception e) {
            resultList.addAll(records);
        }
        return resultList;
    }
}
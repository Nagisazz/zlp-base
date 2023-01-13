package com.nagisazz.base.config.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.util.CollectionUtils;

import com.nagisazz.base.entity.LogRecord;
import com.nagisazz.base.property.ZlpProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * LogStorage
 */
@Slf4j
@RequiredArgsConstructor
public class LogStorage {

    private final LogQueue logQueue;

    private final AsyncSendLogHandler asyncSendLogHandler;

    private final ZlpProperties.LogProperties logProperties;

    private static final Lock lock = new ReentrantLock();

    // 临时存储
    private final List<LogRecord> logList = Collections.synchronizedList(new ArrayList<LogRecord>());

    // 将临时队列加入缓冲队列的线程池
    private static final ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1);

    public void init() {
        scheduledPool.scheduleAtFixedRate(new LogQueueStorageTask(), 0, logProperties.getStorageInterval(), TimeUnit.SECONDS);
    }

    public void saveLog(LogRecord logRecord) {
        lock.lock();
        logList.add(logRecord);
        lock.unlock();
    }

    class LogQueueStorageTask implements Runnable {

        public LogQueueStorageTask() {
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    log.info("JVM关闭时执行");
                    // 队列中的日志
                    List<List<LogRecord>> queueAll = logQueue.getAll();
                    // 集合中的日志
                    List<List<LogRecord>> logSeparateAll = logSeparate();
                    // 发送给平台
                    queueAll.addAll(logSeparateAll);
                    if (!CollectionUtils.isEmpty(queueAll)) {
                        queueAll.forEach(logRecords -> {
                            log.info("jvm关闭时，开始发送线程中的集合，一共{}条数据", logRecords.size());
                            asyncSendLogHandler.sendLogs(logRecords);
                        });
                    }
                }
            }));
        }

        @Override
        public void run() {
            List<List<LogRecord>> logSeparateList = logSeparate();
            for (List<LogRecord> logRecordList : logSeparateList) {
                if (!CollectionUtils.isEmpty(logRecordList)) {
                    boolean flag = putLogs2Queue(logRecordList);
                    if (!flag) {
                        logList.addAll(logRecordList);
                    }
                }
            }
        }

        /**
         * 临时队列转入缓冲队列
         *
         * @param logRecords
         * @return
         */
        private boolean putLogs2Queue(List<LogRecord> logRecords) {
            return logQueue.offer(logRecords);
        }

        /**
         * 将日志分割成指定大小
         *
         * @return
         */
        private List<List<LogRecord>> logSeparate() {
            List<List<LogRecord>> separateList = new ArrayList<List<LogRecord>>();
            lock.lock();
            List<LogRecord> auditLogs = new ArrayList<LogRecord>(logList);
            logList.clear();
            lock.unlock();
            if (CollectionUtils.isEmpty(auditLogs)) {
                return separateList;
            }
            int size = auditLogs.size();
            // 长度不大于指定大小
            if (size <= logProperties.getListSize()) {
                separateList.add(auditLogs);
                return separateList;
            }
            // 长度大于50
            int times = size / logProperties.getListSize();
            for (int i = 0; i <= times; i++) {
                List<LogRecord> every = new ArrayList<LogRecord>();
                int begin = i * logProperties.getListSize();
                int end = (i + 1) * logProperties.getListSize();
                if (end > size) {
                    end = size;
                }
                every.addAll(auditLogs.subList(begin, end));
                if (CollectionUtils.isEmpty(every)) {
                    continue;
                }
                separateList.add(every);
            }
            return separateList;
        }
    }
}
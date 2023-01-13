package com.nagisazz.base.config.log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.nagisazz.base.entity.LogRecord;

/**
 * log缓冲队列
 */
public class LogQueue {

    private final Integer queueLimit;

    private LinkedBlockingQueue<List<LogRecord>> queue = new LinkedBlockingQueue<>(100);

    public LogQueue(Integer queueLimit) {
        this.queueLimit = queueLimit;
    }

    public void init() {
        setQueue(new LinkedBlockingQueue<>(queueLimit));
    }

    private void setQueue(LinkedBlockingQueue<List<LogRecord>> q) {
        queue = q;
    }

    public boolean offer(List<LogRecord> auditLogs) {
        return queue.offer(auditLogs);
    }

    public List<LogRecord> take() throws InterruptedException {
        return queue.take();
    }

    public List<List<LogRecord>> getAll() {
        List<List<LogRecord>> result = new ArrayList<>();
        queue.drainTo(result);
        return result;
    }

    public int size() {
        return queue.size();
    }
}
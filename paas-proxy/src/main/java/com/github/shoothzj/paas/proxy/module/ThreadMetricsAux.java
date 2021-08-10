package com.github.shoothzj.paas.proxy.module;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hezhangjian
 */
@Slf4j
public class ThreadMetricsAux {

    private long usedNanoTime;

    private long lastNanoTime;

    public ThreadMetricsAux() {
    }

    public ThreadMetricsAux(long usedNanoTime, long lastNanoTime) {
        this.usedNanoTime = usedNanoTime;
        this.lastNanoTime = lastNanoTime;
    }

    public long getUsedNanoTime() {
        return usedNanoTime;
    }

    public void setUsedNanoTime(long usedNanoTime) {
        this.usedNanoTime = usedNanoTime;
    }

    public long getLastNanoTime() {
        return lastNanoTime;
    }

    public void setLastNanoTime(long lastNanoTime) {
        this.lastNanoTime = lastNanoTime;
    }
}

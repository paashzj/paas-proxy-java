package com.github.shoothzj.paas.proxy.service;

import com.github.shoothzj.paas.proxy.module.ThreadMetricsAux;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author hezhangjian
 */
@Slf4j
@Service
public class ThreadMetricService {

    @Autowired
    private MeterRegistry meterRegistry;

    private final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

    private final HashMap<Long, ThreadMetricsAux> map = new HashMap<>();

    private final ArrayList<Tag> tags = new ArrayList<>();

    /**
     * one minutes
     */
    @Scheduled(cron = "0 * * * * ?")
    public void schedule() {
        final long[] allThreadIds = threadBean.getAllThreadIds();
        for (long threadId : allThreadIds) {
            final ThreadInfo threadInfo = threadBean.getThreadInfo(threadId);
            if (threadInfo == null) {
                continue;
            }
            final long threadNanoTime = getThreadCPUTime(threadId);
            if (threadNanoTime == 0) {
                // abnormal, clean map
                map.remove(threadId);
            }
            // check if map has data
            final long nanoTime = System.nanoTime();
            ThreadMetricsAux oldMetrics = map.get(threadId);
            if (oldMetrics != null) {
                double percent = (double) (threadNanoTime - oldMetrics.getUsedNanoTime()) / (double) (nanoTime - oldMetrics.getLastNanoTime());
                final Tags tagsWithThreadName = Tags.concat(tags, "threadName", threadInfo.getThreadName());
                meterRegistry.gauge("jvm.threads.cpu", tagsWithThreadName, percent);
            }
            map.put(threadId, new ThreadMetricsAux(threadNanoTime, nanoTime));
        }
    }

    long getThreadCPUTime(long threadId) {
        long time = threadBean.getThreadCpuTime(threadId);
        /* thread of the specified ID is not alive or does not exist */
        return time == -1 ? 0 : time;
    }

}

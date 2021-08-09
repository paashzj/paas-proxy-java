package com.github.shoothzj.paas.proxy.pulsar.http.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @author hezhangjian
 */
@Configuration
@Service
public class PulsarConfig {

    @Value("${PULSAR_HOST:localhost}")
    public String pulsarHost;

    @Value("${PULSAR_PORT:8080}")
    public int pulsarPort;

    @Value("${PULSAR_IO_THREADS:4}")
    public int pulsarIoThreads;

    @Value("${PULSAR_OPERATION_TIMEOUT_SECONDS:15}")
    public int pulsarOperationTimeoutSeconds;

    @Value("${PULSAR_PRODUCER_BATCH:false}")
    public boolean pulsarProducerBatch;

    @Value("${PULSAR_PRODUCER_BATCH_DELAY_MS:1")
    public long pulsarProducerBatchDelayMs;

}

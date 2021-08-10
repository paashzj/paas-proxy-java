package com.github.shoothzj.paas.proxy.pulsar.service;

import com.github.shoothzj.paas.proxy.pulsar.config.PulsarConfig;
import com.github.shoothzj.paas.proxy.pulsar.module.TopicKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.ProducerBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author hezhangjian
 */
@Slf4j
@Service
public class PulsarClientService {

    private final PulsarClient pulsarClient;

    private final PulsarConfig pulsarConfig;

    public PulsarClientService(@Autowired PulsarConfig pulsarConfig) {
        try {
            pulsarClient = PulsarClient.builder()
                    .operationTimeout(pulsarConfig.operationTimeoutSeconds, TimeUnit.SECONDS)
                    .ioThreads(pulsarConfig.ioThreads)
                    .serviceUrl(String.format("http://%s:%s", pulsarConfig.host, pulsarConfig.port))
                    .build();
            this.pulsarConfig = pulsarConfig;
        } catch (Exception e) {
            log.error("create pulsar client exception ", e);
            throw new IllegalArgumentException("build pulsar client exception, exit");
        }
    }

    public Producer<byte[]> createProducer(TopicKey topicKey) throws Exception {
        ProducerBuilder<byte[]> builder = pulsarClient.newProducer().enableBatching(true);
        builder = builder.maxPendingMessages(pulsarConfig.producerMaxPendingMessage);
        builder = builder.autoUpdatePartitions(pulsarConfig.autoUpdatePartition);
        if (pulsarConfig.producerBatch) {
            builder = builder.enableBatching(true);
            builder = builder.batchingMaxPublishDelay(pulsarConfig.producerBatchDelayMs, TimeUnit.MILLISECONDS);
        } else {
            builder = builder.enableBatching(false);
        }
        return builder.topic(concatTopicFn(topicKey)).create();
    }

    private String concatTopicFn(TopicKey topicKey) {
        return String.format("persistent://%s/%s/%s", topicKey.getTenant(), topicKey.getNamespace(), topicKey.getTopic());
    }

}

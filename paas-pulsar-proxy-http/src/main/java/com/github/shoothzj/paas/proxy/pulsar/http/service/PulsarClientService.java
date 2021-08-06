package com.github.shoothzj.paas.proxy.pulsar.http.service;

import com.github.shoothzj.paas.proxy.pulsar.http.config.PulsarConfig;
import com.github.shoothzj.paas.proxy.pulsar.http.module.TopicKey;
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

    public PulsarClientService(@Autowired PulsarConfig pulsarConfig) {
        try {
            pulsarClient = PulsarClient.builder()
                    .operationTimeout(pulsarConfig.pulsarOperationTimeoutSeconds, TimeUnit.SECONDS)
                    .ioThreads(pulsarConfig.pulsarIoThreads)
                    .serviceUrl(String.format("http://%s:%s", pulsarConfig.pulsarHost, pulsarConfig.pulsarPort))
                    .build();
        } catch (Exception e) {
            log.error("create pulsar client exception ", e);
            throw new IllegalArgumentException("build pulsar client exception, exit");
        }
    }

    public Producer<byte[]> createProducer(TopicKey topicKey) throws Exception {
        ProducerBuilder<byte[]> producerBuilder = pulsarClient.newProducer().enableBatching(true).maxPendingMessages(100);
        return producerBuilder.topic(concatTopicFn(topicKey)).create();
    }

    private String concatTopicFn(TopicKey topicKey) {
        return String.format("persistent://%s/%s/%s", topicKey.getTenant(), topicKey.getNamespace(), topicKey.getTopic());
    }

}

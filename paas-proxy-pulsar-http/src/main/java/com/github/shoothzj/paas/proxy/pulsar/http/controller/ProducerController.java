package com.github.shoothzj.paas.proxy.pulsar.http.controller;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.shoothzj.paas.common.module.Semantic;
import com.github.shoothzj.paas.common.proxy.http.module.ProduceMsgReq;
import com.github.shoothzj.paas.common.proxy.http.module.ProduceMsgResp;
import com.github.shoothzj.paas.proxy.pulsar.config.PulsarConfig;
import com.github.shoothzj.paas.proxy.pulsar.module.TopicKey;
import com.github.shoothzj.paas.proxy.pulsar.service.PulsarClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author hezhangjian
 */
@Slf4j
@RestController
@RequestMapping(path = "/v1/pulsar")
public class ProducerController {

    @Autowired
    private PulsarClientService pulsarClientService;

    @Autowired
    private PulsarConfig pulsarConfig;

    private AsyncLoadingCache<TopicKey, Producer<byte[]>> producerCache;

    private final Random RANDOM = new Random();

    @PostConstruct
    public void init() {
        this.producerCache = Caffeine.newBuilder()
                .expireAfterAccess(600, TimeUnit.SECONDS)
                .maximumSize(3000)
                .removalListener((RemovalListener<TopicKey, Producer<byte[]>>) (key, value, cause) -> {
                    log.info("topic {} cache removed, because of {}", key.getTopic(), cause);
                    try {
                        value.close();
                    } catch (Exception e) {
                        log.error("close failed, ", e);
                    }
                })
                .buildAsync(new AsyncCacheLoader<TopicKey, Producer<byte[]>>() {
                    @Override
                    public CompletableFuture<Producer<byte[]>> asyncLoad(TopicKey key, Executor executor) {
                        return acquireFuture(key);
                    }

                    @Override
                    public CompletableFuture<Producer<byte[]>> asyncReload(TopicKey key, Producer<byte[]> oldValue,
                                                                           Executor executor) {
                        return acquireFuture(key);
                    }
                });
    }

    @PostMapping(path = "/tenants/{tenant}/namespaces/{namespace}/topics/{topic}/produce")
    public Mono<ResponseEntity<ProduceMsgResp>> produce(@PathVariable(name = "tenant") String tenant, @PathVariable(name = "namespace") String namespace,
                                                        @PathVariable(name = "topic") String topic, @RequestBody ProduceMsgReq produceMsgReq) {
        CompletableFuture<ResponseEntity<ProduceMsgResp>> future = new CompletableFuture<>();
        long startTime = System.currentTimeMillis();
        int random = pulsarConfig.topicRandom;
        if (random > 0) {
            int index = RANDOM.nextInt(random);
            topic = topic + "_" + index;
        }
        final CompletableFuture<Producer<byte[]>> cacheFuture = producerCache.get(new TopicKey(tenant, namespace, topic));
        String finalTopic = topic;
        cacheFuture.whenComplete((producer, e) -> {
            if (e != null) {
                log.error("create pulsar client exception ", e);
                future.complete(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                return;
            }
            try {
                producer.sendAsync(produceMsgReq.getMsg().getBytes(StandardCharsets.UTF_8)).whenComplete(((messageId, throwable) -> {
                    if (throwable != null) {
                        log.error("send producer msg error ", throwable);
                        return;
                    }
                    log.info("topic {} send success, msg id is {}", finalTopic, messageId);
                    if (pulsarConfig.producerSemantic.equals(Semantic.AT_LEAST_ONCE)) {
                        future.complete(new ResponseEntity<>(new ProduceMsgResp(System.currentTimeMillis() - startTime), HttpStatus.OK));
                    }
                }));
                if (pulsarConfig.producerSemantic.equals(Semantic.AT_MOST_ONCE)) {
                    future.complete(new ResponseEntity<>(new ProduceMsgResp(System.currentTimeMillis() - startTime), HttpStatus.OK));
                }
            } catch (Exception ex) {
                log.error("send async failed ", ex);
                future.complete(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            }
        });
        return Mono.fromFuture(future);
    }

    private CompletableFuture<Producer<byte[]>> acquireFuture(TopicKey topicKey) {
        CompletableFuture<Producer<byte[]>> future = new CompletableFuture<>();
        try {
            future.complete(pulsarClientService.createProducer(topicKey));
        } catch (Exception e) {
            log.error("create producer exception ", e);
            future.completeExceptionally(e);
        }
        return future;
    }


}

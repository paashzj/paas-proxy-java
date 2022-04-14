/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.github.shoothzj.paas.proxy.pulsar.config;

import com.github.shoothzj.paas.common.module.Semantic;
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
    public String host;

    @Value("${PULSAR_PORT:8080}")
    public int port;

    @Value("${PULSAR_IO_THREADS:4}")
    public int ioThreads;

    @Value("${PULSAR_TOPIC_SUFFIX_NUM:0}")
    public int topicSuffixNum;

    @Value("${PULSAR_AUTO_UPDATE_PARTITION:false}")
    public boolean autoUpdatePartition;

    @Value("${PULSAR_PRODUCER_SEMANTIC:AT_LEAST_ONCE}")
    public Semantic producerSemantic;

    @Value("${PULSAR_OPERATION_TIMEOUT_SECONDS:15}")
    public int operationTimeoutSeconds;

    @Value("${PULSAR_PRODUCER_MAX_PENDING_MESSAGE:100}")
    public int producerMaxPendingMessage;

    @Value("${PULSAR_PRODUCER_BATCH:false}")
    public boolean producerBatch;

    @Value("${PULSAR_PRODUCER_BATCH_DELAY_MS:1}")
    public long producerBatchDelayMs;

    @Value("${PULSAR_PRODUCER_CACHE_SECONDS:600}")
    public long producerCacheSeconds;

    @Value("${PULSAR_PRODUCER_MAX_SIZE:3000}")
    public long producerMaxSize;

    @Value("${CONNECTION_TIMEOUT_SECONDS:15}")
    public int connectionTimeoutSeconds;

    @Value("${PULSAR_TENANT_PREFIX_NAME:}")
    public String tenantPrefixName;

    @Value("${PULSAR_TENANT_SUFFIX_NUM:1}")
    public int tenantSuffixNum;

    @Value("${PULSAR_TENANT_SUFFIX_NUM_OF_DIGITS:0}")
    public int tenantSuffixNumOfDigits;

    @Value("${PULSAR_NAMESPACE_PREFIX_NAME:}")
    public String namespacePrefixName;

    @Value("${PULSAR_NAMESPACE_SUFFIX_NUM:1}")
    public int namespaceSuffixNum;

    @Value("${PULSAR_NAMESPACE_SUFFIX_NUM_OF_DIGITS:0}")
    public int namespaceSuffixNumOfDigits;

    @Value("${PULSAR_TTL_SECONDS:60}")
    public int ttlInSeconds;

    @Value("${PULSAR_QUOTA_BYTES:1024}")
    public int quotaBytes;

    @Value("${PULSAR_RETENTION_TIMES_MINUTES:2880}")
    public int retentionTimes;

    @Value("${PULSAR_RETENTION_SIZE:1024}")
    public int retentionSize;
}

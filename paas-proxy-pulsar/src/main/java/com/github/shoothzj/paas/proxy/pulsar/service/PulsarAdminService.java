/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.shoothzj.paas.proxy.pulsar.service;

import com.github.shoothzj.paas.proxy.pulsar.config.PulsarConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.Namespaces;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.common.policies.data.BacklogQuota;
import org.apache.pulsar.common.policies.data.RetentionPolicies;
import org.apache.pulsar.common.policies.data.TenantInfoImpl;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PulsarAdminService {

    private final PulsarAdmin pulsarAdmin;

    private final PulsarConfig pulsarConfig;

    public PulsarAdminService(PulsarConfig pulsarConfig) {
        try {
            pulsarAdmin = PulsarAdmin.builder()
                    .connectionTimeout(pulsarConfig.connectionTimeoutSeconds, TimeUnit.SECONDS)
                    .serviceHttpUrl(String.format("http://%s:%s", pulsarConfig.host, pulsarConfig.port)).build();
            this.pulsarConfig = pulsarConfig;
        } catch (Exception e) {
            log.error("create pulsar admin exception ", e);
            throw new IllegalArgumentException("build pulsar admin exception, exit");
        }
    }

    public void init() {

        log.info("begin create tenant and namespace.");
        if (!pulsarConfig.tenantPrefixName.isBlank()) {
            for (int i = 0; i <= pulsarConfig.tenantSuffixNum; i++) {
                String tenantName = createName(pulsarConfig.tenantPrefixName, i, pulsarConfig.tenantSuffixNumOfDigits);
                try {
                    createTenants(tenantName);
                    log.info("【PPJ】success to create tenant [{}].", tenantName);
                    if (!pulsarConfig.namespacePrefixName.isBlank()) {
                        createNamespace(tenantName);
                    }
                } catch (PulsarAdminException e) {
                    log.error("【PPJ】fail to create tenant [{}]. ", tenantName, e);
                }
            }
        }

    }

    private void createTenants(String tenantName) throws PulsarAdminException {
        pulsarAdmin.tenants().createTenant(tenantName, new TenantInfoImpl());
    }

    private void createNamespace(String tenant) {
        for (int j = 0; j < pulsarConfig.namespaceSuffixNum; j++) {
            String namespaceName = createName(pulsarConfig.tenantPrefixName,
                    j, pulsarConfig.tenantSuffixNumOfDigits);
            try {
                String namespace = String.format("%s/%s", tenant, namespaceName);
                RetentionPolicies retentionPolicies =
                        new RetentionPolicies(pulsarConfig.retentionTimes, pulsarConfig.retentionSize);
                Namespaces namespaces = pulsarAdmin.namespaces();
                namespaces.createNamespace(namespace);
                namespaces.setBacklogQuota(namespace,
                        BacklogQuota.builder().limitSize(pulsarConfig.quotaBytes).build());
                namespaces.setNamespaceMessageTTL(namespace, pulsarConfig.ttlInSeconds);
                namespaces.setRetention(namespace, retentionPolicies);
                log.info("【PPJ】success to create namespace [{}].", namespaceName);
            } catch (PulsarAdminException e) {
                log.error("【PPJ】fail to create namespace [{}].", namespaceName, e);
            }
        }
    }

    private String createName(String prefixName, int suffixNum, int numOfDigits) {
        int suffix = (int) Math.pow(10, numOfDigits) + suffixNum;
        return String.format("%s_%s", prefixName, suffix);
    }

}

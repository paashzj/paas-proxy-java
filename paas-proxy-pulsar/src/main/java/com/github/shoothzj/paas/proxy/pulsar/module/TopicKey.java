package com.github.shoothzj.paas.proxy.pulsar.module;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

/**
 * @author hezhangjian
 */
@Data
@AllArgsConstructor
public class TopicKey {

    private String tenant;

    private String namespace;

    private String topic;

    public TopicKey() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopicKey topicKey = (TopicKey) o;

        if (!Objects.equals(tenant, topicKey.tenant)) return false;
        if (!Objects.equals(namespace, topicKey.namespace)) return false;
        return Objects.equals(topic, topicKey.topic);
    }

    @Override
    public int hashCode() {
        int result = tenant != null ? tenant.hashCode() : 0;
        result = 31 * result + (namespace != null ? namespace.hashCode() : 0);
        result = 31 * result + (topic != null ? topic.hashCode() : 0);
        return result;
    }
}

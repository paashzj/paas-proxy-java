package com.github.shoothzj.paas.proxy.pulsar.http.controller;

import com.github.shoothzj.paas.common.proxy.http.module.ProduceMsgReq;
import com.github.shoothzj.paas.proxy.test.config.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProducerControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testProduceMsgNull() throws Exception {
        ProduceMsgReq produceMsgReq = new ProduceMsgReq();
        ResponseEntity<String> entity = template.postForEntity("/v1/pulsar/tenants/public/namespaces/default/topics/topic/produce",
                produceMsgReq, String.class);
        log.info("entity is {}", entity);
        Assertions.assertEquals(500, entity.getStatusCodeValue());
    }

}

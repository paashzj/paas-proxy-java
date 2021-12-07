package com.github.shoothzj.paas.proxy;

import com.github.shoothzj.paas.proxy.pulsar.http.controller.ProducerController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MainTest {

    @Autowired
    private ProducerController producerController;

    @Test
    public void testContextLoad() {
        Assertions.assertNotNull(producerController);
    }

}
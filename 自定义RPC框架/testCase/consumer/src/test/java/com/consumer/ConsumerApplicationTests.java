package com.consumer;

import com.consumer.service.ServiceConsume;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConsumerApplicationTests {

    @Autowired
    ServiceConsume serviceConsume;

    @Test
    void contextLoads() {
        serviceConsume.consume();
    }

}

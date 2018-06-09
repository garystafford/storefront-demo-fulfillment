package com.storefront.kafka;

import com.storefront.model.FulfillmentRequestEvent;
import com.storefront.respository.FulfillmentRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class Receiver {

    @Autowired
    private FulfillmentRequestRepository fulfillmentRequestRepository;

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {

        return latch;
    }

    @KafkaListener(topics = "${spring.kafka.topic.orders-order}")
    public void receive(FulfillmentRequestEvent fulfillmentRequestEvent) {

        log.info("received payload='{}'", fulfillmentRequestEvent.toString());
        latch.countDown();

        fulfillmentRequestRepository.save(fulfillmentRequestEvent);
    }
}
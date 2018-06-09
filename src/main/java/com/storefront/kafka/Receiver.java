package com.storefront.kafka;

import com.storefront.model.Fulfillment;
import com.storefront.model.FulfillmentEvent;
import com.storefront.respository.FulfillmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class Receiver {

    @Autowired
    private FulfillmentRepository fulfillmentRepository;

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {

        return latch;
    }

    @KafkaListener(topics = "${spring.kafka.topic.orders-order}")
    public void receive(FulfillmentEvent fulfillmentEvent) {

        log.info("received payload='{}'", fulfillmentEvent.toString());
        latch.countDown();
        Fulfillment fulfillment = new Fulfillment();
        fulfillment.setId(fulfillmentEvent.getId());
        fulfillment.setTimestamp(fulfillmentEvent.getTimestamp());
        fulfillment.setName(fulfillmentEvent.getName());
        fulfillment.setContact(fulfillmentEvent.getContact());
        fulfillment.setAddress(fulfillmentEvent.getAddress());
        fulfillment.setOrder(fulfillmentEvent.getOrder());
        fulfillmentRepository.save(fulfillment);
    }
}
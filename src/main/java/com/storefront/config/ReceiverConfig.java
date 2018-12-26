package com.storefront.config;

import com.storefront.kafka.Receiver;
import com.storefront.model.FulfillmentRequestEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import java.util.Map;

public interface ReceiverConfig {

    @Bean
    Map<String, Object> consumerConfigs();

    @Bean
    ConsumerFactory<String, FulfillmentRequestEvent> consumerFactory();

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, FulfillmentRequestEvent> kafkaListenerContainerFactory();

    @Bean
    Receiver receiver();
}

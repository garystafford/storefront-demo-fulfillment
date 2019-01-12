package com.storefront.config;

import com.storefront.kafka.Sender;
import com.storefront.model.OrderStatusChangeEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Profile("!gke")
@Configuration
@EnableKafka
public class SenderConfigNotConfluent implements SenderConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Override
    @Bean
    public Map<String, Object> producerConfigs() {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }

    @Override
    @Bean
    public ProducerFactory<String, OrderStatusChangeEvent> producerFactory() {

        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Override
    @Bean
    public KafkaTemplate<String, OrderStatusChangeEvent> kafkaTemplate() {

        return new KafkaTemplate<>(producerFactory());
    }

    @Override
    @Bean
    public Sender sender() {

        return new Sender();
    }
}
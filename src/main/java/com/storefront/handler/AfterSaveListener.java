package com.storefront.handler;

import com.storefront.kafka.Sender;
import com.storefront.model.FulfillmentRequest;
import com.storefront.model.Order;
import com.storefront.model.OrderStatusEvent;
import com.storefront.model.OrderStatusEventChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
public class AfterSaveListener extends AbstractMongoEventListener<FulfillmentRequest> {

    private Sender sender;

    @Value("${spring.kafka.topic.fulfillment-order}")
    private String topic;

    @Autowired
    public AfterSaveListener(Sender sender) {

        this.sender = sender;
    }

    @Override
    public void onAfterSave(AfterSaveEvent<FulfillmentRequest> event) {

        log.info("onAfterSave event='{}'", event);
        FulfillmentRequest fulfillmentRequest = event.getSource();
        List<OrderStatusEvent> orderStatusEvents = fulfillmentRequest.getOrder().getOrderStatusEvents();
        OrderStatusEventChange orderStatusEventChange = new OrderStatusEventChange();

        Order order = fulfillmentRequest.getOrder();
        orderStatusEventChange.setGuid(order.getGuid());
        orderStatusEventChange.setOrderStatusEvent(orderStatusEvents.get(0));

        sender.send(topic, orderStatusEventChange);
    }
}
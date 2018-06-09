package com.storefront.controller;

import com.storefront.kafka.Sender;
import com.storefront.model.*;
import com.storefront.respository.FulfillmentRepository;
import com.storefront.utilities.SampleData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/fulfillment")
public class FulfillmentController {

    private Sender sender;

    private FulfillmentRepository fulfillmentRepository;

    private MongoTemplate mongoTemplate;

    @Value("${spring.kafka.topic.fulfillment-order}")
    private String topic;

    @Autowired
    public FulfillmentController(Sender sender,
                                 FulfillmentRepository fulfillmentRepository,
                                 MongoTemplate mongoTemplate) {

        this.sender = sender;
        this.fulfillmentRepository = fulfillmentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @RequestMapping(path = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, List<FulfillmentEvent>>> fulfillmentRequestSummary() {

        List<FulfillmentEvent> fulfillmentEvents = fulfillmentRepository.findAll();
        return new ResponseEntity<>(Collections.singletonMap("fulfillmentEvents", fulfillmentEvents), HttpStatus.OK);
    }

    @RequestMapping(path = "/sample/process", method = RequestMethod.GET)
    public ResponseEntity<String> changeOrderStatusToProcessing() {

        List<FulfillmentEvent> fulfillmentEvents = getFulfillmentRequestEvents(OrderStatusType.APPROVED);

        log.info("Approved fulfillment requests count: " + fulfillmentEvents.size() + '\n');

        for (FulfillmentEvent fulfillmentEvent : fulfillmentEvents) {
            processFulfillmentRequestEvent(fulfillmentEvent, OrderStatusType.PROCESSING);
        }

        return new ResponseEntity("Order status changed to 'Processing'", HttpStatus.OK);
    }

    @RequestMapping(path = "/sample/ship", method = RequestMethod.GET)
    public ResponseEntity<String> changeOrderStatusToShipped() {

        List<FulfillmentEvent> fulfillmentEvents = getFulfillmentRequestEvents(OrderStatusType.PROCESSING);

        log.info("Processing fulfillment requests count: " + fulfillmentEvents.size() + '\n');

        for (FulfillmentEvent fulfillmentEvent : fulfillmentEvents) {
            fulfillmentEvent.setShippingMethod(SampleData.getRandomShippingMethod());
            processFulfillmentRequestEvent(fulfillmentEvent, OrderStatusType.SHIPPED);
        }

        return new ResponseEntity("Order status changed 'Shipped'", HttpStatus.OK);
    }

    @RequestMapping(path = "/sample/in-transit", method = RequestMethod.GET)
    public ResponseEntity<String> changeOrderStatusToInTransit() {

        List<FulfillmentEvent> fulfillmentEvents = getFulfillmentRequestEvents(OrderStatusType.SHIPPED);

        log.info("Shipped fulfillment requests count: " + fulfillmentEvents.size() + '\n');

        for (FulfillmentEvent fulfillmentEvent : fulfillmentEvents) {
            processFulfillmentRequestEvent(fulfillmentEvent, OrderStatusType.IN_TRANSIT);
        }

        return new ResponseEntity("Order status changed 'In Transit'", HttpStatus.OK);
    }

    @RequestMapping(path = "/sample/receive", method = RequestMethod.GET)
    public ResponseEntity<String> changeOrderStatusToReceived() {

        List<FulfillmentEvent> fulfillmentEvents = getFulfillmentRequestEvents(OrderStatusType.IN_TRANSIT);

        log.info("In Transit fulfillment requests count: " + fulfillmentEvents.size() + '\n');

        for (FulfillmentEvent fulfillmentEvent : fulfillmentEvents) {
            processFulfillmentRequestEvent(fulfillmentEvent, OrderStatusType.RECEIVED);
        }

        return new ResponseEntity("Order status changed 'Received'", HttpStatus.OK);
    }

    private List<FulfillmentEvent> getFulfillmentRequestEvents(OrderStatusType orderStatusType) {

        Criteria elementMatchCriteria = Criteria.where("order.orderStatusEvents")
                .elemMatch(Criteria.where("orderStatusType").is(orderStatusType));
        Query query = Query.query(elementMatchCriteria);
        return mongoTemplate.find(query, FulfillmentEvent.class);
    }

    private void processFulfillmentRequestEvent(FulfillmentEvent fulfillmentEvent, OrderStatusType orderStatusType) {

        List<OrderStatusEvent> orderStatusEvents = new ArrayList<>();
        OrderStatusEvent orderStatusEvent = new OrderStatusEvent(orderStatusType);
        orderStatusEvents.add(orderStatusEvent);

        Order order = fulfillmentEvent.getOrder();
        order.setOrderStatusEvents(orderStatusEvents);
        fulfillmentEvent.setOrder(order);
        fulfillmentRepository.save(fulfillmentEvent);
        sendOrderStatusEvent(orderStatusEvent, order);
    }

    private void sendOrderStatusEvent(OrderStatusEvent orderStatusEvent, Order order) {

        OrderStatusChangeEvent orderStatusChangeEvent = new OrderStatusChangeEvent(
                order.getGuid(),
                orderStatusEvent
        );
        sender.send(topic, orderStatusChangeEvent);
    }
}

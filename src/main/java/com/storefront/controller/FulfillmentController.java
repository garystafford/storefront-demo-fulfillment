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
@RequestMapping("/fulfillments")
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
    public ResponseEntity<Map<String, List<Fulfillment>>> fulfillmentRequestSummary() {

        List<Fulfillment> fulfillments = fulfillmentRepository.findAll();
        return new ResponseEntity<>(Collections.singletonMap("fulfillments", fulfillments), HttpStatus.OK);
    }

    @RequestMapping(path = "/sample/process", method = RequestMethod.GET)
    public ResponseEntity<String> changeOrderStatusToProcessing() {

        List<Fulfillment> fulfillments = getFulfillments(OrderStatusType.APPROVED);

        log.info("Approved fulfillment requests count: " + fulfillments.size() + '\n');

        for (Fulfillment fulfillment : fulfillments) {
            processFulfillment(fulfillment, OrderStatusType.PROCESSING);
        }

        return new ResponseEntity("Order status changed to 'Processing'", HttpStatus.OK);
    }

    @RequestMapping(path = "/sample/ship", method = RequestMethod.GET)
    public ResponseEntity<String> changeOrderStatusToShipped() {

        List<Fulfillment> fulfillments = getFulfillments(OrderStatusType.PROCESSING);

        log.info("Processing fulfillment requests count: " + fulfillments.size() + '\n');

        for (Fulfillment fulfillment : fulfillments) {
            fulfillment.setShippingMethod(SampleData.getRandomShippingMethod());
            processFulfillment(fulfillment, OrderStatusType.SHIPPED);
        }

        return new ResponseEntity("Order status changed to 'Shipped'", HttpStatus.OK);
    }

    @RequestMapping(path = "/sample/in-transit", method = RequestMethod.GET)
    public ResponseEntity<String> changeOrderStatusToInTransit() {

        List<Fulfillment> fulfillments = getFulfillments(OrderStatusType.SHIPPED);

        log.info("Shipped fulfillment requests count: " + fulfillments.size() + '\n');

        for (Fulfillment fulfillment : fulfillments) {
            processFulfillment(fulfillment, OrderStatusType.IN_TRANSIT);
        }

        return new ResponseEntity("Order status changed to 'In Transit'", HttpStatus.OK);
    }

    @RequestMapping(path = "/sample/receive", method = RequestMethod.GET)
    public ResponseEntity<String> changeOrderStatusToReceived() {

        List<Fulfillment> fulfillments = getFulfillments(OrderStatusType.IN_TRANSIT);

        log.info("In Transit fulfillment requests count: " + fulfillments.size() + '\n');

        for (Fulfillment fulfillment : fulfillments) {
            processFulfillment(fulfillment, OrderStatusType.RECEIVED);
        }

        return new ResponseEntity("Order status changed to 'Received'", HttpStatus.OK);
    }

    private List<Fulfillment> getFulfillments(OrderStatusType orderStatusType) {

        Criteria elementMatchCriteria = Criteria.where("order.orderStatusEvents")
                .elemMatch(Criteria.where("orderStatusType").is(orderStatusType));
        Query query = Query.query(elementMatchCriteria);
        return mongoTemplate.find(query, Fulfillment.class);
    }

    private void processFulfillment(Fulfillment fulfillment, OrderStatusType orderStatusType) {

        List<OrderStatusEvent> orderStatusEvents = new ArrayList<>();
        OrderStatusEvent orderStatusEvent = new OrderStatusEvent(orderStatusType);
        orderStatusEvents.add(orderStatusEvent);

        Order order = fulfillment.getOrder();
        order.setOrderStatusEvents(orderStatusEvents);
        fulfillment.setOrder(order);
        fulfillmentRepository.save(fulfillment);
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

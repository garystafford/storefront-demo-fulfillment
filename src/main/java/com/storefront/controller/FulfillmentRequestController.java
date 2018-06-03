package com.storefront.controller;

import com.storefront.model.FulfillmentRequest;
import com.storefront.model.Order;
import com.storefront.model.OrderStatusEvent;
import com.storefront.model.OrderStatusType;
import com.storefront.respository.FulfillmentRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/fulfillmentRequests")
public class FulfillmentRequestController {

    private FulfillmentRequestRepository fulfillmentRequestRepository;

    private MongoTemplate mongoTemplate;

    @Autowired
    public FulfillmentRequestController(FulfillmentRequestRepository fulfillmentRequestRepository,
                                        MongoTemplate mongoTemplate) {

        this.fulfillmentRequestRepository = fulfillmentRequestRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @RequestMapping(path = "/sample", method = RequestMethod.GET)
    public ResponseEntity<String> sampleData() {

        Criteria elementMatchCriteria = Criteria.where("orders.orderStatusEvents")
                .elemMatch(Criteria.where("orderStatusType").is(OrderStatusType.APPROVED));
        Query query = Query.query(elementMatchCriteria);
        List<FulfillmentRequest> fulfillmentRequests = mongoTemplate.find(query, FulfillmentRequest.class);

        log.info("fulfillmentRequests size: " + fulfillmentRequests.size() + '\n');

        for (FulfillmentRequest fulfillmentRequest : fulfillmentRequests) {
            List<OrderStatusEvent> orderStatusEvents = new ArrayList<>();
            orderStatusEvents.add(new OrderStatusEvent(OrderStatusType.APPROVED));
            Order order = fulfillmentRequest.getOrder();
            order.setOrderStatusEvents(orderStatusEvents);
            fulfillmentRequest.setOrder(order);

            fulfillmentRequestRepository.save(fulfillmentRequest);
        }

        return new ResponseEntity("Order statuses changed", HttpStatus.OK);
    }

    @RequestMapping(path = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, List<FulfillmentRequest>>> fulfillmentRequestSummary() {

        List<FulfillmentRequest> fulfillmentRequestList = fulfillmentRequestRepository.findAll();
        return new ResponseEntity<>(Collections.singletonMap("fulfillmentRequests", fulfillmentRequestList), HttpStatus.OK);
    }
}

package com.storefront.controller;

import com.storefront.model.FulfillmentRequest;
import com.storefront.respository.FulfillmentRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fulfillmentRequests")
public class OrderController {

    private FulfillmentRequestRepository fulfillmentRequestRepository;


    @Autowired
    public OrderController(FulfillmentRequestRepository fulfillmentRequestRepository) {
        this.fulfillmentRequestRepository = fulfillmentRequestRepository;
    }

    @RequestMapping(path = "/sample", method = RequestMethod.GET)
    public ResponseEntity<String> sampleData() {

//        List<Customer> customerList = orderRepository.findAll();
//
//        for (Customer customer : customerList) {
//            customer.setOrders(Utility.createSampleOrderHistory());
//        }
//
//        orderRepository.saveAll(customerList);

        return new ResponseEntity("Order statuses changed", HttpStatus.OK);
    }

    @RequestMapping(path = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, List<FulfillmentRequest>>> orderSummary() {

        List<FulfillmentRequest> fulfillmentRequestList = fulfillmentRequestRepository.findAll();
        return new ResponseEntity<>(Collections.singletonMap("fulfillmentRequests", fulfillmentRequestList), HttpStatus.OK);
    }
}

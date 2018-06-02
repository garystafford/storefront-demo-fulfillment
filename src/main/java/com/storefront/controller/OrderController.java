package com.storefront.controller;

import com.storefront.model.Order;
import com.storefront.respository.OrderRepository;
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
@RequestMapping("/orders")
public class OrderController {

    private OrderRepository orderRepository;


    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
    public ResponseEntity<Map<String, List<Order>>> orderSummary() {

        List<Order> orderList = orderRepository.findAll();
        return new ResponseEntity<>(Collections.singletonMap("orders", orderList), HttpStatus.OK);
    }
}

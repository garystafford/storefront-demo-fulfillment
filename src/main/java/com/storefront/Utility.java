package com.storefront;

import com.storefront.model.Order;
import com.storefront.model.OrderItem;
import com.storefront.model.Product;
import com.storefront.model.Status;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class Utility {

    public Utility() {
    }

    public static List<Product> createSampleProducts() {

        List<Product> productList = new ArrayList<>();
        productList.add(new Product("b5efd4a0-4eb9-4ad0-bc9e-2f5542cbe897", "Blue Widget", "Lovely Blue Widget", new BigDecimal("1.99")));
        productList.add(new Product("d01fde07-7c24-49c5-a5f1-bc2ce1f14c48", "Red Widget", "Lovely Red Widget", new BigDecimal("3.99")));
        productList.add(new Product("a9d5a5c7-4245-4b4e-b1c3-1d3968f36b2d", "Yellow Widget", "Lovely Yellow Widget", new BigDecimal("5.99")));
        productList.add(new Product("4efe33a1-722d-48c8-af8e-7879edcad2fa", "Purple Widget", "Lovely Purple Widget", new BigDecimal("7.99")));
        productList.add(new Product("f3b9bdce-10d8-4c22-9861-27149879b3c1", "Orange Widget", "Lovely Orange Widget", new BigDecimal("9.99")));
        productList.add(new Product("7f3c9c22-3c0a-47a5-9a92-2bd2e23f6e37", "Green Widget", "Lovely Green Widget", new BigDecimal("11.99")));
        productList.add(new Product("b506b962-fcfa-4ad6-a955-8859797edf16", "Black Widget", "Lovely Black Widget", new BigDecimal("13.99")));
        productList.add(new Product("c8810c1d-b0ea-486b-acfa-7724bb70f5e6", "White Widget", "Lovely White Widget", new BigDecimal("15.99")));

        return productList;
    }

    public static List<Order> createSampleOrderHistory() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem("b5efd4a0-4eb9-4ad0-bc9e-2f5542cbe897", 2, new BigDecimal("1.99")));
        orderItems.add(new OrderItem("a9d5a5c7-4245-4b4e-b1c3-1d3968f36b2d", 4, new BigDecimal("5.99")));
        orderItems.add(new OrderItem("f3b9bdce-10d8-4c22-9861-27149879b3c1", 1, new BigDecimal("9.99")));
        orderItems.add(new OrderItem("b506b962-fcfa-4ad6-a955-8859797edf16", 3, new BigDecimal("13.99")));

        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order(timestamp.getTime(), Status.COMPLETED, orderItems));

        orderItems = new ArrayList<>();
        orderItems.add(new OrderItem("d01fde07-7c24-49c5-a5f1-bc2ce1f14c48", 5, new BigDecimal("3.99")));
        orderItems.add(new OrderItem("4efe33a1-722d-48c8-af8e-7879edcad2fa", 2, new BigDecimal("7.99")));
        orderItems.add(new OrderItem("7f3c9c22-3c0a-47a5-9a92-2bd2e23f6e37", 4, new BigDecimal("11.99")));

        orderList.add(new Order(timestamp.getTime(), Status.PROCESSING, orderItems));

        return orderList;
    }
}

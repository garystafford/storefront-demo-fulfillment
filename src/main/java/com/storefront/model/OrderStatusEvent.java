package com.storefront.model;

import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;

@Data
public class OrderStatusEvent {

    @NonNull
    private Long timestamp;

    @NonNull
    private OrderStatusType orderStatusType;

    private String note;

    public OrderStatusEvent() {

        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
    }

    public OrderStatusEvent(OrderStatusType orderStatusType) {

        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.orderStatusType = orderStatusType;
    }

    public OrderStatusEvent(OrderStatusType orderStatusType, String note) {

        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.orderStatusType = orderStatusType;
        this.note = note;
    }
}


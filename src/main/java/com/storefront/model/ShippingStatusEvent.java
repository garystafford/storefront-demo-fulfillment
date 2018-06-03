package com.storefront.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class ShippingStatusEvent {

    @NonNull
    private Long timestamp;

    @NonNull
    private ShippingStatusType shippingStatusType;

    private String note;

    public ShippingStatusEvent(ShippingStatusType shippingStatusType) {

        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.shippingStatusType = shippingStatusType;
    }

    public ShippingStatusEvent(ShippingStatusType shippingStatusType, String note) {

        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.shippingStatusType = shippingStatusType;
        this.note = note;
    }
}


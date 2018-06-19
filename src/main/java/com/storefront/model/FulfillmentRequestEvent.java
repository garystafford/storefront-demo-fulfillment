package com.storefront.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FulfillmentRequestEvent {

    @Id
    private String id;

    @NonNull
    private Long timestamp;

    @NonNull
    private Name name;

    @NonNull
    private Contact contact;

    @NonNull
    private Address address;

    @NonNull
    private Order order;
}

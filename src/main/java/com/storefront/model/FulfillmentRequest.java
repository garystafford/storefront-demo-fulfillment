package com.storefront.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "fulfillment.requests")
public class FulfillmentRequest {

    @Id
    private String id;

    @NotNull
    private Long timestamp;

    @NonNull
    private Name name;

    @NonNull
    private Contact contact;

    @NonNull
    private Address address;

    @NotNull
    private Order order;

}

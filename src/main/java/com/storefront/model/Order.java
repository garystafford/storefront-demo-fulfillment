package com.storefront.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @NotNull
    private Long timestamp;

    @NotNull
    private Status status;

    @NotNull
    private Customer customer;

    @NotNull
    private List<OrderItem> orderItems;

}

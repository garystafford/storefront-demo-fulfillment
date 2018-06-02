package com.storefront.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @NotNull
    private Long timestamp;

    @NotNull
    private OrderStatus orderStatus;

    @NotNull
    private List<OrderItem> orderItems;
}

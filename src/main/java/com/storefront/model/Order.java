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

//    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
//    private java.util.Date timestamp;

    @NotNull
    private Long timestamp;

    @NotNull
    private Status status;

    @NotNull
    private List<OrderItem> orderItems;

}

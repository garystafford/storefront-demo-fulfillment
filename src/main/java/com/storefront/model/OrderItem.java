package com.storefront.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @NonNull
    private String productGuid;

    @NonNull
    private Integer quantity;

    @NonNull
    private BigDecimal unitPrice;
}

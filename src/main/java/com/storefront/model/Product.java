package com.storefront.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Document
public class Product {

    @Id
    private String id;

    @NonNull
    private String guid;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private BigDecimal price;
}

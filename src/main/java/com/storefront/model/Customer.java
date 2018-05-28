package com.storefront.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Customer {

    @Id
    private String id;

    @NonNull
    private String accountsId;

    @NonNull
    private Name name;

    @NonNull
    private Contact contact;

    @NonNull
    private List<Order> orders;
}

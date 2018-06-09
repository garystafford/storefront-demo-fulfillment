package com.storefront.respository;

import com.storefront.model.Fulfillment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FulfillmentRepository extends MongoRepository<Fulfillment, String> {

}
package com.storefront.respository;

import com.storefront.model.FulfillmentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FulfillmentRequestRepository extends MongoRepository<FulfillmentRequest, String> {
}
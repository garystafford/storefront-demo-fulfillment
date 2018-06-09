package com.storefront.respository;

import com.storefront.model.FulfillmentRequestEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FulfillmentRequestRepository extends MongoRepository<FulfillmentRequestEvent, String> {

}
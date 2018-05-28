package com.storefront.controller;

import com.storefront.Utility;
import com.storefront.respository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @RequestMapping(path = "/sample", method = RequestMethod.GET)
    public ResponseEntity<String> createSampleData() {

        productRepository.deleteAll();
        productRepository.saveAll(Utility.createSampleProducts());

        return new ResponseEntity("Sample data created", HttpStatus.CREATED);
    }


}

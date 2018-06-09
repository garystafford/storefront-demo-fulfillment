package com.storefront.utilities;

import com.storefront.model.ShippingMethod;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SampleData {

    public static ShippingMethod getRandomShippingMethod() {

        switch (new Random().nextInt(4)) {
            case 0:
                return ShippingMethod.Drone;
            case 1:
                return ShippingMethod.FedEx;
            case 2:
                return ShippingMethod.Uber;
            case 3:
                return ShippingMethod.UPS;
            default:
                return ShippingMethod.USPS;
        }
    }
}
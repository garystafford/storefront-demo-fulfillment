package com.storefront;

import com.storefront.model.Order;
import com.storefront.model.OrderItem;
import com.storefront.model.Product;
import com.storefront.respository.CustomerRepository;
import com.storefront.respository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class Utility {

    private CustomerRepository customerRepository;
    private ProductRepository productRepository;

    @Autowired
    public Utility(CustomerRepository customerRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public void createTestProducts() {

        productRepository.deleteAll();

        List<Product> products = new ArrayList<>();
        products.add(new Product("b5efd4a0-4eb9-4ad0-bc9e-2f5542cbe897", "Blue Widget", "Lovely Blue Widget", new BigDecimal("1.99")));
        products.add(new Product("d01fde07-7c24-49c5-a5f1-bc2ce1f14c48", "Red Widget", "Lovely Red Widget", new BigDecimal("3.99")));
        products.add(new Product("a9d5a5c7-4245-4b4e-b1c3-1d3968f36b2d", "Yellow Widget", "Lovely Yellow Widget", new BigDecimal("5.99")));
        products.add(new Product("4efe33a1-722d-48c8-af8e-7879edcad2fa", "Purple Widget", "Lovely Purple Widget", new BigDecimal("7.99")));
        products.add(new Product("f3b9bdce-10d8-4c22-9861-27149879b3c1", "Orange Widget", "Lovely Orange Widget", new BigDecimal("9.99")));
        products.add(new Product("7f3c9c22-3c0a-47a5-9a92-2bd2e23f6e37", "Green Widget", "Lovely Green Widget", new BigDecimal("11.99")));
        products.add(new Product("b506b962-fcfa-4ad6-a955-8859797edf16", "Black Widget", "Lovely Black Widget", new BigDecimal("13.99")));
        products.add(new Product("c8810c1d-b0ea-486b-acfa-7724bb70f5e6", "White Widget", "Lovely White Widget", new BigDecimal("15.99")));
    }

    public List<Order> createOrderHistory() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem("b5efd4a0-4eb9-4ad0-bc9e-2f5542cbe897", 2, new BigDecimal("1.99")));
        orderItems.add(new OrderItem("a9d5a5c7-4245-4b4e-b1c3-1d3968f36b2d", 4, new BigDecimal("5.99")));
        orderItems.add(new OrderItem("f3b9bdce-10d8-4c22-9861-27149879b3c1", 1, new BigDecimal("9.99")));
        orderItems.add(new OrderItem("b506b962-fcfa-4ad6-a955-8859797edf16", 3, new BigDecimal("13.99")));

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(timestamp.getTime(), orderItems));

        orderItems = new ArrayList<>();
        orderItems.add(new OrderItem("d01fde07-7c24-49c5-a5f1-bc2ce1f14c48", 5, new BigDecimal("3.99")));
        orderItems.add(new OrderItem("4efe33a1-722d-48c8-af8e-7879edcad2fa", 2, new BigDecimal("7.99")));
        orderItems.add(new OrderItem("7f3c9c22-3c0a-47a5-9a92-2bd2e23f6e37", 4, new BigDecimal("11.99")));

        orders.add(new Order(timestamp.getTime(), orderItems));

        return orders;
    }

    public void createTestCustomers() {
        customerRepository.deleteAll();

//        // New Customer 1
//        Name name = new Name();
//        name.setTitle("Mr.");
//        name.setFirstName("John");
//        name.setMiddleName("S.");
//        name.setLastName("Doe");
//        name.setSuffix("Jr.");
//
//        Contact contact = new Contact();
//        contact.setPrimaryPhone("555-666-7777");
//        contact.setSecondaryPhone("555-444-9898");
//        contact.setEmail("john.doe@internet.com");
//
//        List<Address> addressList = new ArrayList<>();
//
//        Address address = new Address();
//        address.setType(AddressType.BILLING);
//        address.setDescription("My cc billing address");
//        address.setAddress1("123 Oak Street");
//        address.setCity("Sunrise");
//        address.setState("CA");
//        address.setPostalCode("12345-6789");
//
//        addressList.add(address);
//
//        address = new Address();
//        address.setType(AddressType.SHIPPING);
//        address.setDescription("My home address");
//        address.setAddress1("123 Oak Street");
//        address.setCity("Sunrise");
//        address.setState("CA");
//        address.setPostalCode("12345-6789");
//
//        addressList.add(address);
//
//        List<CreditCard> creditCardList = new ArrayList<>();
//
//        CreditCard creditCard = new CreditCard();
//        creditCard.setType(CreditCardType.PRIMARY);
//        creditCard.setDescription("VISA");
//        creditCard.setNumber("1234-6789-0000-0000");
//        creditCard.setExpiration("6/19");
//        creditCard.setNameOnCard("John S. Doe");
//
//        creditCardList.add(creditCard);
//
//        creditCard = new CreditCard();
//        creditCard.setType(CreditCardType.ALTERNATE);
//        creditCard.setDescription("Corporate American Express");
//        creditCard.setNumber("9999-8888-7777-6666");
//        creditCard.setExpiration("3/20");
//        creditCard.setNameOnCard("John Doe");
//
//        creditCardList.add(creditCard);
//
//        Credentials credentials = new Credentials();
//        credentials.setUsername("johndoe37");
//        credentials.setPassword("skd837#$hfh485&");
//
//        Customer newCustomer = new Customer();
//        newCustomer.setName(name);
//        newCustomer.setContact(contact);
//        newCustomer.setAddresses(addressList);
//        newCustomer.setCreditCards(creditCardList);
//        newCustomer.setCredentials(credentials);
//
//        customerRepository.save(newCustomer);
//
//        // New Customer 2
//        name = new Name();
//        name.setTitle("Ms.");
//        name.setFirstName("Mary");
//        name.setLastName("Smith");
//
//        contact = new Contact();
//        contact.setPrimaryPhone("456-789-0001");
//        contact.setSecondaryPhone("456-222-1111");
//        contact.setEmail("marysmith@yougotmail.com");
//
//        addressList = new ArrayList<>();
//
//        address = new Address();
//        address.setType(AddressType.BILLING);
//        address.setDescription("My CC billing address");
//        address.setAddress1("1234 Main Street");
//        address.setCity("Anywhere");
//        address.setState("NY");
//        address.setPostalCode("45455-66677");
//
//        addressList.add(address);
//
//        address = new Address();
//        address.setType(AddressType.SHIPPING);
//        address.setDescription("Home Sweet Home");
//        address.setAddress1("1234 Main Street");
//        address.setCity("Anywhere");
//        address.setState("NY");
//        address.setPostalCode("45455-66677");
//
//        addressList.add(address);
//
//        creditCardList = new ArrayList<>();
//
//        creditCard = new CreditCard();
//        creditCard.setType(CreditCardType.PRIMARY);
//        creditCard.setDescription("VISA");
//        creditCard.setNumber("4545-6767-8989-0000");
//        creditCard.setExpiration("7/21");
//        creditCard.setNameOnCard("Mary Smith");
//
//        creditCardList.add(creditCard);
//
//        credentials = new Credentials();
//        credentials.setUsername("msmith445");
//        credentials.setPassword("S*$475hf&*dddFFG3");
//
//        newCustomer = new Customer();
//        newCustomer.setName(name);
//        newCustomer.setContact(contact);
//        newCustomer.setAddresses(addressList);
//        newCustomer.setCreditCards(creditCardList);
//        newCustomer.setCredentials(credentials);
//
//        customerRepository.save(newCustomer);
//
//        // fetch all customers
//        System.out.println("Customers found with findAll():");
//        System.out.println("-------------------------------");
//        for (Customer customer : customerRepository.findAll()) {
//            System.out.println(customer);
//        }
//    }

    }
}

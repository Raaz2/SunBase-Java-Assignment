package com.sunbase.CustomerCRUD.service;

import com.sunbase.CustomerCRUD.model.Customer;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface ExternalAPIService {

    // Method to authenticate and get Bearer token
    String authenticate();

    // fetching customers list from external api using token
    List<Customer> fetchCustomers(String token);


}

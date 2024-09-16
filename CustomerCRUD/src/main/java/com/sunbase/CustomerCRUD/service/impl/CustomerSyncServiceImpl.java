package com.sunbase.CustomerCRUD.service.impl;

import com.sunbase.CustomerCRUD.exception.CustomerException;
import com.sunbase.CustomerCRUD.model.Customer;
import com.sunbase.CustomerCRUD.repository.CustomerRepository;
import com.sunbase.CustomerCRUD.service.CustomerService;
import com.sunbase.CustomerCRUD.service.CustomerSyncService;
import com.sunbase.CustomerCRUD.service.ExternalAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerSyncServiceImpl implements CustomerSyncService {

    @Autowired
    private ExternalAPIService externalApiService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    // Method to sync customers
    @Transactional
    public void syncCustomers() throws CustomerException {

        String token = externalApiService.authenticate();

        List<Customer> externalCustomers = externalApiService.fetchCustomers(token);

        for (Customer externalCustomer : externalCustomers) {
            String uuid = externalCustomer.getUuid();

            Customer existingCustomer = customerRepository.findById(uuid).orElse(null);
            if (existingCustomer != null) {
                customerService.updateCustomer(uuid, externalCustomer);
            } else {
                customerRepository.save(externalCustomer);
            }
        }
    }
}

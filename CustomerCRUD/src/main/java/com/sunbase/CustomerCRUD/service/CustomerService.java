package com.sunbase.CustomerCRUD.service;

import com.sunbase.CustomerCRUD.exception.CustomerException;
import com.sunbase.CustomerCRUD.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    Customer createCustomer(Customer customer) throws CustomerException;

    Customer updateCustomer(String id, Customer customerDetails) throws CustomerException;

    // Retrieve a customer by ID
    Customer getCustomerById(String id) throws CustomerException;

    // Delete a customer by ID
    Customer deleteCustomer(String id) throws CustomerException;

    List<Customer> saveAll(List<Customer> customers) throws CustomerException;

    Page<Customer> searchByFirstOrLastName(String keyword, Pageable pageable);

    Page<Customer> searchByCity(String keyword, Pageable pageable);

    Page<Customer> searchByEmail(String keyword, Pageable pageable);

    Page<Customer> searchByPhone(String keyword, Pageable pageable);

    // Retrieve all customers
    Page<Customer> getAllCustomers(Pageable pageable) throws CustomerException;

}

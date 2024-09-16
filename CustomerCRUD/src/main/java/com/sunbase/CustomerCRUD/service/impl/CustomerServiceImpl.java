package com.sunbase.CustomerCRUD.service.impl;

import com.sunbase.CustomerCRUD.exception.CustomerException;
import com.sunbase.CustomerCRUD.model.Customer;
import com.sunbase.CustomerCRUD.repository.CustomerRepository;
import com.sunbase.CustomerCRUD.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) throws CustomerException {
         return customerRepository.save(customer);
    }

    public Customer updateCustomer(String id, Customer customerDetails) throws CustomerException {
        // Fetch the customer by ID
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();

            // Update customer details only if provided
            if (customerDetails.getFirstName() != null && !customerDetails.getFirstName().isEmpty()) {
                customer.setFirstName(customerDetails.getFirstName());
            }

            if (customerDetails.getLastName() != null && !customerDetails.getLastName().isEmpty()) {
                customer.setLastName(customerDetails.getLastName());
            }

            if (customerDetails.getStreet() != null && !customerDetails.getStreet().isEmpty()) {
                customer.setStreet(customerDetails.getStreet());
            }

            if (customerDetails.getAddress() != null && !customerDetails.getAddress().isEmpty()) {
                customer.setAddress(customerDetails.getAddress());
            }

            if (customerDetails.getCity() != null && !customerDetails.getCity().isEmpty()) {
                customer.setCity(customerDetails.getCity());
            }

            if (customerDetails.getState() != null && !customerDetails.getState().isEmpty()) {
                customer.setState(customerDetails.getState());
            }

            if (customerDetails.getEmail() != null && !customerDetails.getEmail().isEmpty()) {
                customer.setEmail(customerDetails.getEmail());
            }

            if (customerDetails.getPhone() != null && !customerDetails.getPhone().isEmpty()) {
                customer.setPhone(customerDetails.getPhone());
            }

            // Save the updated customer back to the repository
            return customerRepository.save(customer);
        } else {
            throw new CustomerException("Customer not found with id: " + id);
        }
    }

    // Retrieve a customer by ID
    public Customer getCustomerById(String id) throws CustomerException {
        // Fetch the customer by ID
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("Customer not found with id: " + id));
    }

    // Delete a customer by ID
    public Customer deleteCustomer(String id) throws CustomerException {
        // Fetch the customer by ID
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("Customer not found with id: " + id));
        // Delete the customer
        customerRepository.delete(customer);
        return customer;
    }

    public List<Customer> saveAll(List<Customer> customers) throws CustomerException {
        if (customers == null || customers.isEmpty()) {
            throw new CustomerException("Customer list cannot be null or empty");
        }

        // Save all customers in bulk
        return customerRepository.saveAll(customers);
    }

    public Page<Customer> searchByFirstOrLastName(String keyword, Pageable pageable) {
        return customerRepository.searchByFirstOrLastName(keyword, pageable);
    }

    public Page<Customer> searchByCity(String keyword, Pageable pageable) {
        return customerRepository.searchByCity(keyword, pageable);
    }

    public Page<Customer> searchByEmail(String keyword, Pageable pageable) {
        return customerRepository.searchByEmail(keyword, pageable);
    }

    public Page<Customer> searchByPhone(String keyword, Pageable pageable) {
        return customerRepository.searchByPhone(keyword, pageable);
    }

    // Retrieve all customers
    public Page<Customer> getAllCustomers(Pageable pageable) throws CustomerException {
        Page<Customer> customers = customerRepository.findAll(pageable);
        if (customers.isEmpty()) {
            throw new CustomerException("No customers found");
        }
        return customers;
    }

}

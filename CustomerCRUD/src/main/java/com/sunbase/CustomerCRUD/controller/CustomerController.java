package com.sunbase.CustomerCRUD.controller;

import com.sunbase.CustomerCRUD.exception.CustomerException;
import com.sunbase.CustomerCRUD.model.Customer;
import com.sunbase.CustomerCRUD.service.CustomerService;
import com.sunbase.CustomerCRUD.service.CustomerSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerSyncService customerSyncService;

    @PostMapping
    public ResponseEntity<Customer> createCustomerHandler(@RequestBody Customer customer) throws CustomerException {
         Customer saved = customerService.createCustomer(customer);
         return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomerHandler(@PathVariable String id, @RequestBody Customer customerDetails) throws CustomerException {
        Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Customer>> getAllCustomersHandler(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) throws CustomerException {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Customer> customers = customerService.getAllCustomers(pageable);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }


    @GetMapping("/search/city")
    public ResponseEntity<Page<Customer>> searchCustomersByCity(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "city") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) throws CustomerException {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Customer> result = customerService.searchByCity(keyword, pageable);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/phone")
    public ResponseEntity<Page<Customer>> searchCustomersByPhone(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "phone") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) throws CustomerException {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Customer> result = customerService.searchByPhone(keyword, pageable);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/search/email")
    public ResponseEntity<Page<Customer>> searchCustomersByEmail(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "email") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) throws CustomerException {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Customer> result = customerService.searchByEmail(keyword, pageable);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/search/name")
    public ResponseEntity<Page<Customer>> searchCustomersByName(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) throws CustomerException {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Customer> result = customerService.searchByFirstOrLastName(keyword, pageable);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerByIdHandler(@PathVariable String id) throws CustomerException {
        Customer customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomerHandler(@PathVariable String id) throws CustomerException {
        Customer customer = customerService.deleteCustomer(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Customer>> saveAllCustomersHandler(@RequestBody List<Customer> customers) throws CustomerException {
        List<Customer> savedCustomers = customerService.saveAll(customers);
        return new ResponseEntity<>(savedCustomers, HttpStatus.CREATED);
    }


    // Customer Sync controller method for external api calling
    @PostMapping("/sync")
    public ResponseEntity<String> syncCustomers() throws CustomerException {
        customerSyncService.syncCustomers();
        return new ResponseEntity<>("Customers synced successfully!", HttpStatus.OK);
    }


}

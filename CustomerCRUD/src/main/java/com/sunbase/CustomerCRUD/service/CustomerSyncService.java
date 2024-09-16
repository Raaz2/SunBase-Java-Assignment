package com.sunbase.CustomerCRUD.service;

import com.sunbase.CustomerCRUD.exception.CustomerException;
import org.springframework.stereotype.Service;

@Service
public interface CustomerSyncService {
    // Method to sync customers
    void syncCustomers() throws CustomerException;
}

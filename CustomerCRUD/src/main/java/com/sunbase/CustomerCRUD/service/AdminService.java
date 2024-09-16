package com.sunbase.CustomerCRUD.service;

import com.sunbase.CustomerCRUD.exception.AdminException;
import com.sunbase.CustomerCRUD.model.Admin;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    Admin register(Admin user) throws AdminException;
    String verify(Admin admin) throws AdminException;
}

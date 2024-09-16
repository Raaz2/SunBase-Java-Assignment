package com.sunbase.CustomerCRUD.controller;

import com.sunbase.CustomerCRUD.exception.AdminException;
import com.sunbase.CustomerCRUD.model.Admin;
import com.sunbase.CustomerCRUD.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AdminService service;

    @PostMapping("/register")
    public ResponseEntity<Admin> register(@RequestBody Admin admin) throws AdminException {
        Admin registeredAdmin =  service.register(admin);
        return new ResponseEntity<>(registeredAdmin, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String login(@RequestBody Admin admin) throws AdminException {
        return service.verify(admin);
    }

}

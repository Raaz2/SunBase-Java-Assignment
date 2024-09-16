package com.sunbase.CustomerCRUD.service.impl;

import com.sunbase.CustomerCRUD.exception.AdminException;
import com.sunbase.CustomerCRUD.model.Admin;
import com.sunbase.CustomerCRUD.repository.AdminRepository;
import com.sunbase.CustomerCRUD.service.AdminService;
import com.sunbase.CustomerCRUD.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AdminRepository repo;

    @Autowired
    AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(15);
    public Admin register(Admin admin) throws AdminException {
        // Check if loginId is already taken
        Optional<Admin> existingAdmin = repo.findByLoginId(admin.getLoginId());
        if (existingAdmin.isPresent()) {
            throw new AdminException("Login ID is already in use. Please choose a different one.");
        }

        // Proceed with registration
        admin.setPassword(encoder.encode(admin.getPassword()));
        return repo.save(admin);

    }

    public String verify(Admin admin) throws AdminException {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getLoginId(), admin.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(admin.getLoginId());
        } else {
            throw new AdminException("Invalid login credentials!");
        }
    }
}

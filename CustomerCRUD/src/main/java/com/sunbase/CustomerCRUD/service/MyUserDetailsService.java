package com.sunbase.CustomerCRUD.service;

import com.sunbase.CustomerCRUD.model.AdminPrincipal;
import com.sunbase.CustomerCRUD.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private final AdminRepository adminRepository;

    public MyUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return adminRepository.findByLoginId(loginId)
                .map(AdminPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with the loginId: " + loginId));
    }
}


package com.sunbase.CustomerCRUD.repository;

import com.sunbase.CustomerCRUD.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByLoginId(String loginId);
}

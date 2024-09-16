package com.sunbase.CustomerCRUD.repository;

import com.sunbase.CustomerCRUD.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR" +
            " LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Customer> searchByFirstOrLastName(@Param("name") String keyword, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.city) LIKE LOWER(CONCAT('%', :city, '%'))")
    Page<Customer> searchByCity(@Param("city") String keyword, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<Customer> searchByEmail(@Param("email") String keyword, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.phone LIKE CONCAT('%', :phone, '%')")
    Page<Customer> searchByPhone(@Param("phone") String keyword, Pageable pageable);

    // Method to retrieve all customers with pagination
//    Page<Customer> findAll(Pageable pageable);
}


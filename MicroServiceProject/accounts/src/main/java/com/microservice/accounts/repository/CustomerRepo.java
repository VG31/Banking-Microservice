package com.microservice.accounts.repository;

import com.microservice.accounts.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Optional<Customer> findByMobileNumber(String mobileNUmber);

}

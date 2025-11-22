package com.web.edutrade.service;

import com.web.edutrade.model.Customer;
import com.web.edutrade.repo.CustomerRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    CustomerRepo customerRepo;

    public void save(@Valid Customer customer) {
        customerRepo.save(customer);
    }
}

package com.web.edutrade.service;

import com.web.edutrade.model.Customer;
import com.web.edutrade.repo.CustomerRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public void save(@Valid Customer customer) {
        customerRepo.save(customer);
    }

    public Customer findById(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public List<Customer> findAll() {
        return customerRepo.findAll();
    }

    public void deleteById(Long id) {
        customerRepo.deleteById(id);
    }
}
package br.com.fantasmagorica.spring6webapp.service;

import br.com.fantasmagorica.spring6webapp.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List<Customer> listCustomers();

    Customer getCustomerById(UUID id);
}

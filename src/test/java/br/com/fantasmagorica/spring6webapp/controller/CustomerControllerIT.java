package br.com.fantasmagorica.spring6webapp.controller;

import br.com.fantasmagorica.spring6webapp.entities.Customer;
import br.com.fantasmagorica.spring6webapp.mappers.CustomerMapper;
import br.com.fantasmagorica.spring6webapp.models.CustomerDTO;
import br.com.fantasmagorica.spring6webapp.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void patchByIdNotFoundTest() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomerPatchById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void patchByIdTest() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);

        final String customerName = "Updated Name";
        customerDTO.setCustomerName(customerName);


        //final String originalUpc = customer.getUpc(); commented bc customer only has one atribute

        ResponseEntity responseEntity = customerController.updateCustomerPatchById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerName);

        //assertThat(updatedCustomer.getUpc()).isEqualTo(originalUpc); commented bc customer only has one atribute
    }
    
    @Test
    void deleteByIdNotFoundTest() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteCustomerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteById() {
        Customer customer = customerRepository.findAll().get(0);
        ResponseEntity responseEntity = customerController.deleteCustomerById(customer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void testUpdateCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateCustomer() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        customerDTO.setCustomerName("New Updated Name");
        customerDTO.setId(null);
        customerDTO.setVersion(null);

        ResponseEntity responseEntity = customerController.updateById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerDTO.getCustomerName());
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("Test New Customer")
                .build();

        ResponseEntity responseEntity = customerController.handlePost(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID uuid = UUID.fromString(locationUUID[4]);

        Optional<Customer> customer = customerRepository.findById(uuid);
        assertThat(customer).isNotEmpty();

    }

    @Test
    void testGetCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            CustomerDTO customerDTO = customerController.getCustomerById(UUID.randomUUID());
        });

    }

    @Test
    void testGetCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());

        assertThat(customerDTO).isNotNull();
        assertThat(customerDTO.getId()).isEqualTo(customer.getId());
    }

    @Test
    void testListCustomers() {
        List<CustomerDTO> customerDTOS = customerController.listCustomers();

        assertThat(customerDTOS.size()).isEqualTo(2);
    }

    @Transactional
    @Test
    void testEmptyList(){
        customerRepository.deleteAll();

        List<CustomerDTO> customerDTOS = customerController.listCustomers();

        assertThat(customerDTOS.size()).isEqualTo(0);
    }
}
package br.com.fantasmagorica.spring6webapp.service;

import br.com.fantasmagorica.spring6webapp.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService{

    private Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        Customer customerA = Customer.builder()
                .customerName("Eder")
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customerB = Customer.builder()
                .customerName("Joao")
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap.put(customerA.getId(), customerA);
        customerMap.put(customerB.getId(), customerB);
    }

    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer getCustomerById(UUID id) {

        log.debug("Get Customer by Id - in service. Id: " + id.toString());

        return customerMap.get(id);
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {
        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .customerName(customer.getCustomerName())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public void updateCustomerById(UUID customerId, Customer customer) {
        Customer existing = customerMap.get(customerId);

        existing.setUpdateDate(LocalDateTime.now());
        existing.setVersion(existing.getVersion() + 1);
        existing.setCustomerName(customer.getCustomerName());

        customerMap.put(customerId, existing);

    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        customerMap.remove(customerId);
    }

    @Override
    public void patchCustomerById(UUID customerId, Customer customer) {
        Customer existing = customerMap.get(customerId);

        if(StringUtils.hasText(customer.getCustomerName())){
            existing.setCustomerName(customer.getCustomerName());
        }

        existing.setUpdateDate(LocalDateTime.now());
        existing.setVersion(existing.getVersion() + 1);

        //customerMap.put(customerId, existing); atualiza de qqr maneira
    }

}

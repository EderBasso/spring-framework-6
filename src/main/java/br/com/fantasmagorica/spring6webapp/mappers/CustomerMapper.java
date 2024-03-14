package br.com.fantasmagorica.spring6webapp.mappers;

import br.com.fantasmagorica.spring6webapp.entities.Customer;
import br.com.fantasmagorica.spring6webapp.models.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDTO(Customer customer);
}

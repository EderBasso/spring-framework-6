package br.com.fantasmagorica.spring6webapp.controller;

import br.com.fantasmagorica.spring6webapp.configuration.SpringSecurityConfig;
import br.com.fantasmagorica.spring6webapp.models.CustomerDTO;
import br.com.fantasmagorica.spring6webapp.services.CustomerService;
import br.com.fantasmagorica.spring6webapp.services.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Import(SpringSecurityConfig.class)
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();

    @Test
    void testGetCustomerList() throws Exception {
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get("/api/v1/customer/")
                        .with(httpBasic("user1", "password"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));

    }

    @Test
    void getCustomerByIdNotFound() throws Exception{
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/customer/{customerId}", UUID.randomUUID())
                        .with(httpBasic("user1", "password")))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCustomerById() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.listCustomers().get(0);

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));

        mockMvc.perform(get("/api/v1/customer/" + testCustomer.getId())
                        .with(httpBasic("user1", "password"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerName" , is(testCustomer.getCustomerName())))
                .andExpect(jsonPath("$.id" , is(testCustomer.getId().toString())));
    }

    @Test
    void testCreateNewCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);
        customer.setVersion(null);
        customer.setId(null);

        given(customerService.saveNewCustomer(any(CustomerDTO.class))).willReturn(customerServiceImpl.listCustomers().get(1));

        mockMvc.perform(post("/api/v1/customer/")
                        .with(httpBasic("user1", "password"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);

        given(customerService.updateCustomerById(any(), any())).willReturn(Optional.of(customer));

        mockMvc.perform(put("/api/v1/customer/" + customer.getId())
                        .with(httpBasic("user1", "password"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(any(UUID.class), any(CustomerDTO.class));
    }


    @Test
    void testDeleteCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);

        given(customerService.deleteCustomerById(any())).willReturn(true);

        mockMvc.perform(delete("/api/v1/customer/" + customer.getId())
                        .with(httpBasic("user1", "password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "New Name");

        given(customerService.patchCustomerById(any(), any())).willReturn(Optional.of(customer));

        mockMvc.perform(patch("/api/v1/customer/" + customer.getId())
                        .with(httpBasic("user1", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerMap.get("customerName")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());
    }
}

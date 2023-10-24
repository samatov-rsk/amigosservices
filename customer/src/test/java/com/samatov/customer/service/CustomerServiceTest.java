package com.samatov.customer.service;

import com.samatov.amqp.RabbitMQMessageProducer;
import com.samatov.clients.fraud.FraudCheckResponse;
import com.samatov.clients.fraud.FraudClient;
import com.samatov.customer.Customer;
import com.samatov.customer.CustomerRegistrationRequest;
import com.samatov.customer.CustomerRepository;
import com.samatov.customer.CustomerService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)

public class CustomerServiceTest {

    @InjectMocks
    public CustomerService customerService;

    @Mock
    public CustomerRepository customerRepository;

    @Mock
    public RabbitMQMessageProducer rabbitMQMessageProducer;

    @Mock
    FraudClient fraudClient;

    @Test
    public void registerCustomerWhenFakeResponseFalse() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Mura", "Samatov", "halfmsk@gmail.com");

        Customer fakeCustomer = new Customer("Mura", "Samatov", "halfmsk@gmail.com");

        FraudCheckResponse fakeResponse = new FraudCheckResponse(false);
        when(fraudClient.isFraudster(fakeCustomer.getId())).thenReturn(fakeResponse);
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(fakeCustomer);
        customerService.registerCustomer(request);
        Mockito.verify(fraudClient, Mockito.times(1)).isFraudster(any());
        Mockito.verify(customerRepository, Mockito.times(1)).saveAndFlush(any());
        Mockito.verify(rabbitMQMessageProducer, Mockito.times(1))
                .publish(any(), anyString(), anyString());
    }

    @Test
    public void registerCustomerWhenFakeResponseTrue() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Mura", "Samatov", "halfmsk@gmail.com");

        Customer fakeCustomer = new Customer("Mura", "Samatov", "halfmsk@gmail.com");

        FraudCheckResponse fakeResponse = new FraudCheckResponse(true);
        when(fraudClient.isFraudster(fakeCustomer.getId())).thenReturn(fakeResponse);
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(fakeCustomer);
        IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> customerService.registerCustomer(request));
        Assertions.assertEquals("fraudster",thrown.getMessage());
    }

}

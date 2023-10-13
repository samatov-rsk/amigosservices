package com.samatov.customer;

import com.samatov.amqp.RabbitMQMessageProducer;
import com.samatov.clients.fraud.FraudCheckResponse;
import com.samatov.clients.fraud.FraudClient;
import com.samatov.clients.notification.NotificationClient;
import com.samatov.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {

   private final CustomerRepository customerRepository;
   private final FraudClient fraudClient;
   private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public void registerCustomer(CustomerRegistrationRequest request){
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());
        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }
        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to Amigoscode,,,", customer.getFirstName())
        );
        rabbitMQMessageProducer.publish(notificationRequest,
                "internal.exchange","internal.notification.routing-key");
    }
}

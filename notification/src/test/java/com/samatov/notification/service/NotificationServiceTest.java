package com.samatov.notification.service;

import com.samatov.clients.notification.NotificationRequest;
import com.samatov.notification.NotificationRepository;
import com.samatov.notification.NotificationService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {

    @InjectMocks
    public NotificationService notificationService;
    @Mock
    public NotificationRepository notificationRepository;

    @Test
    public void send() {
        NotificationRequest notificationRequest = new NotificationRequest(1, "Mura",
                "HelloWorld!");
        notificationService.send(notificationRequest);
        Mockito.verify(notificationRepository, Mockito.times(1))
                .save(any());
    }
}

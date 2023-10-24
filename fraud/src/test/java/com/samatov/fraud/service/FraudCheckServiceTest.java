package com.samatov.fraud.service;

import com.samatov.fraud.FraudCheckHistoryRepository;
import com.samatov.fraud.FraudCheckService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class FraudCheckServiceTest {

    @InjectMocks
    FraudCheckService fraudCheckService;
    @Mock
    FraudCheckHistoryRepository fraudCheckHistoryRepository;

    @Test
    public void isFraudulentCustomer() {
        fraudCheckService.isFraudulentCustomer(1);
        Mockito.verify(fraudCheckHistoryRepository, Mockito.times(1))
                .save(any());
    }
}

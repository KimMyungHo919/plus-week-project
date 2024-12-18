package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.RentalLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RentalLogServiceTest {

    @Mock
    RentalLogRepository rentalLogRepository;

    @InjectMocks
    RentalLogService rentalLogService;

    @Test
    void save() {
        String logMessage = "Hi";
        String logType = "SUCCESS";

        User user1 = new User();
        user1.setId(1L);
        user1.setNickname("user1");

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item1");

        Reservation reservation1 = new Reservation(
                item1, user1, Status.APPROVED,
                LocalDateTime.of(2024, 12, 20, 10, 0),
                LocalDateTime.of(2024, 12, 20, 12, 0)
        );
        reservation1.setId(1L);

        RentalLog rentalLog = new RentalLog(reservation1, logMessage, logType);

        rentalLogService.save(rentalLog);

        // 호출확인
        Mockito.verify(rentalLogRepository).save(rentalLog);
    }
}
package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AdminService adminService;

    @Test
    void reportUsers() {
        List<Long> userIds = List.of(1L, 2L);

        adminService.reportUsers(userIds);

        Mockito.verify(userRepository).blockUsersByIds(userIds);
    }
}
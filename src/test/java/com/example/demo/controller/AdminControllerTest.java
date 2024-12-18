package com.example.demo.controller;

import com.example.demo.dto.ReportRequestDto;
import com.example.demo.interceptor.AuthInterceptor;
import com.example.demo.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false) // 필터 비활성화
@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    AdminService adminService;

    @MockitoBean
    AuthInterceptor authInterceptor;

    @Test
    void reportUsers() throws Exception {
        List<Long> userIds = List.of(1L,2L,3L);

        ReportRequestDto requestDto = new ReportRequestDto(userIds);
        String requestBody = new ObjectMapper().writeValueAsString(requestDto);

        ResultActions result = mockMvc.perform(post("/admins/report-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        result.andExpect(status().isOk());
    }
}
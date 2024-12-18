package com.example.demo.controller;

import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.interceptor.UserRoleInterceptor;
import com.example.demo.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false) // 필터 비활성화
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ReservationService reservationService;

    @MockitoBean
    UserRoleInterceptor userRoleInterceptor;

    @Test
    void createReservation() throws Exception {
        // given
        ReservationRequestDto requestDto = new ReservationRequestDto(
                1L,
                1L,
                LocalDateTime.of(2024, 12, 20, 10, 0),
                LocalDateTime.of(2024, 12, 20, 12, 0)
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 직렬화 처리
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/reservations") // 여기로 보낸다
                        .contentType(MediaType.APPLICATION_JSON) // 요청의 형식. (서버에서 받는 데이터형식)
                        .content(requestBody)) // 요청의 본문. (위에서 작성한거)
                .andExpect(status().isCreated()); // 응답.
    }

    @Test
    void updateReservation() throws Exception {
        Long reservationId = 1L;
        String status = "APPROVED";

        ReservationResponseDto responseDto = new ReservationResponseDto(
                reservationId,
                "user1",
                "item1",
                LocalDateTime.of(2024, 12, 20, 10, 0),
                LocalDateTime.of(2024, 12, 20, 12, 0)
        );

        Mockito.when(reservationService.updateReservationStatus(Mockito.eq(reservationId), Mockito.anyString()))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/reservations/{id}/update-status", reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"APPROVED\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nickname").value("user1"))
                .andExpect(jsonPath("$.itemName").value("item1"));
    }

    @Test
    void findAll() throws Exception {
        List<ReservationResponseDto> reservations = List.of(
                new ReservationResponseDto(
                        1L,
                        "user1",
                        "item1",
                        LocalDateTime.of(2024, 12, 20, 10, 0),
                        LocalDateTime.of(2024, 12, 20, 12, 0)
                ),

                new ReservationResponseDto(
                        2L,
                        "user2",
                        "item2",
                        LocalDateTime.of(2024, 12, 20, 10, 0),
                        LocalDateTime.of(2024, 12, 20, 12, 0)
                )
        );

        Mockito.when(reservationService.getReservations()).thenReturn(reservations);

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].nickname").value("user1"))
                .andExpect(jsonPath("$[0].itemName").value("item1"))

                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].nickname").value("user2"))
                .andExpect(jsonPath("$[1].itemName").value("item2"));
    }

    @Test
    void searchAll() throws Exception {
        List<ReservationResponseDto> reservations = List.of(
                new ReservationResponseDto(
                        1L,
                        "user1",
                        "item1",
                        LocalDateTime.of(2024, 12, 20, 10, 0),
                        LocalDateTime.of(2024, 12, 20, 12, 0)
                )
        );

        Mockito.when(reservationService.searchAndConvertReservations(Mockito.anyLong(), Mockito.anyLong())).thenReturn(reservations);

        mockMvc.perform(get("/reservations/search")
                        .param("userId", "1")
                        .param("itemId", "1"))
                .andExpect(status().isOk());
    }
}

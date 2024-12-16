package com.example.demo.controller;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

    @Test
    void createReservation() throws Exception {
        String requestBody = """
                    {
                        "itemId": 1,
                        "userId": 1,
                        "startAt": "2024-12-20T10:00:00",
                        "endAt": "2024-12-20T12:00:00"
                    }
                """;

        mockMvc.perform(post("/reservations") // 여기로 보낸다
                        .contentType("application/json") // 요청의 형식. (서버에서 받는 데이터형식)
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

        Mockito.when(reservationService.updateReservationStatus(reservationId, status))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/reservations/1/update-status")
                        .contentType("application/json")
                        .content("\"APPROVED\""))
                .andExpect(status().isOk());
        // 여기를 더 채워넣고싶은데..
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
                .andExpect(jsonPath("$[0].nickname").value("user1"));
    }

//    @Test
//    void searchAll() throws Exception {
//        List<ReservationResponseDto> reservations = List.of(
//                new ReservationResponseDto(
//                        1L,
//                        "user1",
//                        "item1",
//                        LocalDateTime.of(2024, 12, 20, 10, 0),
//                        LocalDateTime.of(2024, 12, 20, 12, 0)
//                )
//        );
//
//        Mockito.when(reservationService.searchAndConvertReservations(1L, 1L)).thenReturn(reservations);
//
//        mockMvc.perform(get("/reservations/search")
//                        .param("userId", "1")
//                        .param("itemId", "1"))
//                .andExpect(status().isOk());
//    }
}

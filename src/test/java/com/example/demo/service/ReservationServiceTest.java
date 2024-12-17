package com.example.demo.service;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.*;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RentalLogService rentalLogService;

    @InjectMocks
    private ReservationService reservationService;


    @Test
    void createReservation() {
        // Given
        Long itemId = 1L;
        Long userId = 1L;
        LocalDateTime startAt = LocalDateTime.of(2024, 12, 20, 10, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 12, 20, 12, 0);

        // 가짜 데이터
        Item mockItem = new Item(); // Item 객체 생성
        User mockUser = new User(); // User 객체 생성
        Reservation mockReservation = new Reservation(mockItem, mockUser, Status.PENDING, startAt, endAt);

        // Repository Mock 설정
        Mockito.when(reservationRepository.findConflictingReservations(itemId, startAt, endAt))
                .thenReturn(Collections.emptyList()); // 비어있는걸 받아야함
        Mockito.when(itemRepository.findItemById(itemId)).thenReturn(mockItem);
        Mockito.when(userRepository.findUserById(userId)).thenReturn(mockUser);
        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class)))
                .thenReturn(mockReservation);

        // When
        reservationService.createReservation(itemId, userId, startAt, endAt);

        // Then
        // 1. 충돌 예약 조회
        Mockito.verify(reservationRepository).findConflictingReservations(itemId, startAt, endAt);
        // 2. 아이템 조회
        Mockito.verify(itemRepository).findItemById(itemId);
        // 3. 유저 조회
        Mockito.verify(userRepository).findUserById(userId);
        // 4. 예약 저장
        Mockito.verify(reservationRepository).save(Mockito.any(Reservation.class));
        // 5. 로그 저장
        Mockito.verify(rentalLogService).save(Mockito.any(RentalLog.class));
    }

    @Test
    void getReservations() {
        // Given
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

        User user2 = new User();
        user2.setId(2L);
        user2.setNickname("user2");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");

        Reservation reservation2 = new Reservation(
                item2, user2, Status.APPROVED,
                LocalDateTime.of(2024, 12, 21, 14, 0),
                LocalDateTime.of(2024, 12, 21, 16, 0)
        );
        reservation2.setId(2L);

        List<Reservation> reservations = List.of(reservation1, reservation2);

        // Mock 설정
        Mockito.when(reservationRepository.findAllWithUserAndItem()).thenReturn(reservations);

        // When
        List<ReservationResponseDto> result = reservationService.getReservations();

        // Then
        Assertions.assertEquals(2, result.size()); // 반환된 DTO 개수 확인

        // 첫 번째 예약 확인
        ReservationResponseDto dto1 = result.get(0);
        Assertions.assertEquals(1L, dto1.getId());
        Assertions.assertEquals("user1", dto1.getNickname());
        Assertions.assertEquals("item1", dto1.getItemName());
        Assertions.assertEquals(LocalDateTime.of(2024, 12, 20, 10, 0), dto1.getStartAt());
        Assertions.assertEquals(LocalDateTime.of(2024, 12, 20, 12, 0), dto1.getEndAt());

        // 두 번째 예약 확인
        ReservationResponseDto dto2 = result.get(1);
        Assertions.assertEquals(2L, dto2.getId());
        Assertions.assertEquals("user2", dto2.getNickname());
        Assertions.assertEquals("item2", dto2.getItemName());
        Assertions.assertEquals(LocalDateTime.of(2024, 12, 21, 14, 0), dto2.getStartAt());
        Assertions.assertEquals(LocalDateTime.of(2024, 12, 21, 16, 0), dto2.getEndAt());

        // Mock 메서드 호출 검증
        Mockito.verify(reservationRepository, Mockito.times(1)).findAllWithUserAndItem();
    }

    @Test
    void searchAndConvertReservations() {
        // Given
        Long userId = 1L;
        Long itemId = 2L;

        User user = new User();
        user.setId(userId);
        user.setNickname("user1");

        Item item = new Item();
        item.setId(itemId);
        item.setName("item1");

        Reservation reservation1 = new Reservation(
                item, user, Status.APPROVED,
                LocalDateTime.of(2024, 12, 20, 10, 0),
                LocalDateTime.of(2024, 12, 20, 12, 0)
        );
        reservation1.setId(1L);

        Reservation reservation2 = new Reservation(
                item, user, Status.APPROVED,
                LocalDateTime.of(2024, 12, 21, 14, 0),
                LocalDateTime.of(2024, 12, 21, 16, 0)
        );
        reservation2.setId(2L);

        List<Reservation> reservations = List.of(reservation1, reservation2);

        // Mock 설정
        Mockito.when(reservationRepository.searchReservations(userId, itemId)).thenReturn(reservations);

        // When
        List<ReservationResponseDto> result = reservationService.searchAndConvertReservations(userId, itemId);

        // Then
        Assertions.assertEquals(2, result.size()); // 반환된 DTO 개수 확인

        // 첫 번째 예약 확인
        ReservationResponseDto dto1 = result.get(0);
        Assertions.assertEquals(1L, dto1.getId());
        Assertions.assertEquals("user1", dto1.getNickname());
        Assertions.assertEquals("item1", dto1.getItemName());
        Assertions.assertEquals(LocalDateTime.of(2024, 12, 20, 10, 0), dto1.getStartAt());
        Assertions.assertEquals(LocalDateTime.of(2024, 12, 20, 12, 0), dto1.getEndAt());

        // 두 번째 예약 확인
        ReservationResponseDto dto2 = result.get(1);
        Assertions.assertEquals(2L, dto2.getId());
        Assertions.assertEquals("user1", dto2.getNickname());
        Assertions.assertEquals("item1", dto2.getItemName());
        Assertions.assertEquals(LocalDateTime.of(2024, 12, 21, 14, 0), dto2.getStartAt());
        Assertions.assertEquals(LocalDateTime.of(2024, 12, 21, 16, 0), dto2.getEndAt());

        // Mock 메서드 호출 검증
        Mockito.verify(reservationRepository, Mockito.times(1)).searchReservations(userId, itemId);
    }

    @Test
    void updateReservationStatus() {
        // Given
        Long reservationId = 1L;
        String status = "APPROVED";

        User user = new User();
        user.setId(1L);
        user.setNickname("user1");

        Item item = new Item();
        item.setId(1L);
        item.setName("item1");

        Reservation reservation = new Reservation(
                item,
                user,
                Status.PENDING,
                LocalDateTime.of(2024, 12, 20, 10, 0),
                LocalDateTime.of(2024, 12, 20, 12, 0)
        );
        reservation.setId(reservationId);

        // Mock 설정
        Mockito.when(reservationRepository.findReservationById(reservationId)).thenReturn(reservation);

        // When
        ReservationResponseDto responseDto = reservationService.updateReservationStatus(reservationId, status);

        // Then
        Assertions.assertEquals(Status.APPROVED, reservation.getStatus());
        Assertions.assertEquals(reservationId, responseDto.getId());
        Assertions.assertEquals("user1", responseDto.getNickname());
        Assertions.assertEquals("item1", responseDto.getItemName());
        Assertions.assertEquals(LocalDateTime.of(2024, 12, 20, 10, 0), responseDto.getStartAt());
        Assertions.assertEquals(LocalDateTime.of(2024, 12, 20, 12, 0), responseDto.getEndAt());

        // Mock 메서드 호출 검증
        Mockito.verify(reservationRepository, Mockito.times(1)).findReservationById(reservationId);
    }
}
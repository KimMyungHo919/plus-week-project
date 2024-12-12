package com.example.demo.controller;

import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(@RequestBody ReservationRequestDto reservationRequestDto) {
        reservationService.createReservation(reservationRequestDto.getItemId(),
                reservationRequestDto.getUserId(),
                reservationRequestDto.getStartAt(),
                reservationRequestDto.getEndAt());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}/update-status")
    public ResponseEntity<ReservationResponseDto> updateReservation(
            @PathVariable Long id,
            @RequestBody String status
    ) {
        ReservationResponseDto result = reservationService.updateReservationStatus(id, status);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> findAll() {
        List<ReservationResponseDto> result = reservationService.getReservations();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationResponseDto>> searchAll(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long itemId
    ) {
        List<ReservationResponseDto> result = reservationService.searchAndConvertReservations(userId, itemId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}

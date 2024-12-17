package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicInsert
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'PENDING'")
    private Status status; // PENDING, APPROVED, CANCELED, EXPIRED

    public Reservation(Item item, User user, Status status, LocalDateTime startAt, LocalDateTime endAt) {
        this.item = item;
        this.user = user;
        this.status = status;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Reservation() {}

    public void updateStatus(Status status) {
        this.status = status;
    }
}

package com.example.demo.entity;

public enum Status {
    APPROVED("APPROVED"),
    PENDING("PENDING"),
    CANCELED("CANCELED"),
    EXPIRED("EXPIRED");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}

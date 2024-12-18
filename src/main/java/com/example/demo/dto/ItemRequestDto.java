package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemRequestDto {
    private String name;

    private String description;

    private Long managerId;

    private Long ownerId;
}

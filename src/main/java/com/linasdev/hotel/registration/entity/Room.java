package com.linasdev.hotel.registration.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Room {
    private Integer id;
    private String name;
    private Boolean occupied;
}

package com.linasdev.hotel.registration.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Customer {
    private Integer id;
    private String firstName;
    private String lastName;
    private String roomName;
    private LocalDateTime bookingTime;
    private LocalDateTime checkOutTime;
}

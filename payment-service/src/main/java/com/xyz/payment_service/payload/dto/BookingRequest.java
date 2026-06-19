package com.xyz.payment_service.payload.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
@Data
public class BookingRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Set<Long> serviceIds;
}

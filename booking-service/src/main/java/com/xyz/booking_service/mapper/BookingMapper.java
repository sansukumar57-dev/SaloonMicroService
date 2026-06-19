package com.xyz.booking_service.mapper;

import com.xyz.booking_service.dto.BookingDTO;
import com.xyz.booking_service.model.Booking;

public class BookingMapper {
    public static BookingDTO toDTO(Booking booking){
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());

        bookingDTO.setCustomerId(booking.getCustomerId());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setEndTime(booking.getEndTime());
        bookingDTO.setServiceIds(booking.getServiceIds());
        bookingDTO.setSalonId(booking.getSalonId());
        bookingDTO.setStatus(booking.getStatus());
        return bookingDTO;
    }
}

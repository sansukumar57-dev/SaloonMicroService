package com.xyz.booking_service.service;

import com.xyz.booking_service.domain.BookingStatus;
import com.xyz.booking_service.dto.BookingRequest;
import com.xyz.booking_service.dto.SalonDTO;
import com.xyz.booking_service.dto.ServiceDTO;
import com.xyz.booking_service.dto.UserDTO;
import com.xyz.booking_service.model.Booking;
import com.xyz.booking_service.model.SalonReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {
    Booking createBooking (BookingRequest booking,
                           UserDTO ser,
                           SalonDTO salon,
                           Set<ServiceDTO> serviceDTOSet) throws Exception;

    List<Booking> getBookingByCustomer(Long customerId);

    List<Booking> getBookingBySalon(Long salonId);

    Booking getBookingById(Long id) throws Exception;

    Booking updateBooking(Long bookingId, BookingStatus status) throws Exception;
    List<Booking> getBookingByData(LocalDate date, Long salonId);
    SalonReport getSalonReport(Long salonId);

}

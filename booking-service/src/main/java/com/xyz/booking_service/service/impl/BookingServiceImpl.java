package com.xyz.booking_service.service.impl;

import com.xyz.booking_service.domain.BookingStatus;
import com.xyz.booking_service.dto.BookingRequest;
import com.xyz.booking_service.dto.SalonDTO;
import com.xyz.booking_service.dto.ServiceDTO;
import com.xyz.booking_service.dto.UserDTO;
import com.xyz.booking_service.model.Booking;
import com.xyz.booking_service.model.SalonReport;
import com.xyz.booking_service.repository.BookingRepository;
import com.xyz.booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    @Override
    public Booking createBooking(BookingRequest booking,
                                 UserDTO ser,
                                 SalonDTO salon,
                                 Set<ServiceDTO> serviceDTOSet) throws Exception {


        int totalDuration =serviceDTOSet.stream()
                .mapToInt(ServiceDTO::getDuration)
                .sum();

        LocalDateTime bookingStartTime=booking.getStartTime();
        LocalDateTime bookingEndTime=bookingStartTime.plusMinutes(totalDuration);


  Boolean isSlotAvailable=isTimeSlotAvailable(salon,bookingStartTime,bookingEndTime);
  double totalPrice=serviceDTOSet.stream()
          .mapToDouble(ServiceDTO::getPrice)
    .sum();
Set<Long> idList=serviceDTOSet.stream()
        .map(ServiceDTO::getId)
        .collect(Collectors.toSet());

Booking newBooking=new Booking();

newBooking.setCustomerId(salon.getId());
newBooking.setSalonId(salon.getId());
newBooking.setServiceIds(idList);
newBooking.setStatus(BookingStatus.PENDING);
newBooking.setStartTime(bookingStartTime);
newBooking.setEndTime(bookingEndTime);
newBooking.setTotalPrice((int) totalPrice);

return bookingRepository.save(newBooking);






    }
    public Boolean isTimeSlotAvailable(SalonDTO salonDTO, LocalDateTime bookingStartTime, LocalDateTime bookingEndTime) throws Exception {
       List<Booking> existingBookings=getBookingBySalon(salonDTO.getId());
        LocalDateTime salonOpenTime = salonDTO.getOpenTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime=salonDTO.getCloseTime().atDate(bookingStartTime.toLocalDate());
if(bookingStartTime.isBefore(salonOpenTime)|| bookingEndTime.isAfter(salonCloseTime)){
    throw new Exception("Booking time  must be  within working hours");

}
for(Booking existingBooking:existingBookings){
    LocalDateTime existingBookingStartTime=existingBooking.getStartTime();
    LocalDateTime existingBookingEndTime=existingBooking.getEndTime();
if(bookingStartTime.isBefore(existingBookingEndTime)&&
bookingEndTime.isAfter(existingBookingStartTime)){
    throw new Exception("Slot not available, choose different time");


}
if(bookingStartTime.isEqual(existingBookingStartTime)||bookingEndTime.isEqual(existingBookingEndTime))
{
    throw new Exception("Slot not available, choose different time");
}
}


        return true;
    }

    @Override
    public List<Booking> getBookingByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingBySalon(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Long id) throws Exception {


        Booking booking=bookingRepository.findById(id).orElse(null);
        if(booking==null){
            throw new Exception("Booking not found with id " + id);
        }
        return booking;
    }

    @Override
    public Booking updateBooking(Long bookingId, BookingStatus status) throws Exception {
        Booking booking=getBookingById(bookingId
        );
       booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingByData(LocalDate date, Long salonId) {
       List<Booking> allBookings=getBookingBySalon(salonId);
       if(date==null){
           return allBookings;
       }
       return allBookings.stream().filter(booking->isSameDate(booking.getStartTime(),date)||
               isSameDate(booking.getEndTime(),date)).collect(Collectors.toList());
    }

    private boolean isSameDate(LocalDateTime dateTime, LocalDate date) {
    return dateTime.toLocalDate().isEqual(date);
    }

    @Override
    public SalonReport getSalonReport(Long salonId) {
      List<Booking> bookings=getBookingBySalon(salonId);

      int totalEarnings=bookings.stream().mapToInt(Booking::getTotalPrice).sum();

      Integer totalBookings=bookings.size();
      List<Booking> cancelledBooking=bookings.stream().
              filter(booking -> booking.getStatus().equals(BookingStatus.CANCELLED))
              .collect(Collectors.toList());
      Double totalRefund=cancelledBooking.stream().mapToDouble(Booking::getTotalPrice).sum();

      SalonReport report=new SalonReport();
      report.setSalonId(salonId);
      report.setCancelledBookings(cancelledBooking.size());
      report.setTotalBookings(totalEarnings);
      report.setTotalEarnings(totalEarnings);
      report.setTotalRefund(totalRefund);
      report.setTotalBookings(totalBookings);
      return report;
    }
}

package com.xyz.salonService.service;

import com.xyz.salonService.model.Salon;
import com.xyz.salonService.payload.dto.SalonDTO;
import com.xyz.salonService.payload.dto.UserDTO;

import java.util.List;

public interface SalonService {
    Salon createSalon(SalonDTO salon, UserDTO user);
    Salon upadteSalon(SalonDTO salon, UserDTO user,Long salonId) throws Exception;
    List<Salon> getAllSalons();
    Salon getSalonById(Long salonId)throws Exception;
    Salon getSalonByOwnerId(Long ownerId);
    List<Salon> searchSalonByCity(String city);

}

package com.xyz.salonService.mapper;

import com.xyz.salonService.model.Salon;
import com.xyz.salonService.payload.dto.SalonDTO;

public class SalonMapper {

    public static SalonDTO   mapToDTO(Salon salon){
        SalonDTO salonDto = new SalonDTO();
        salonDto.setId(salon.getId());
        salonDto.setName(salon.getName());
        salonDto.setAddress(salon.getAddress());
        salonDto.setEmail(salon.getEmail());
        salonDto.setCity(salon.getCity());
        salonDto.setImages(salon.getImages());
        salonDto.setOwnerId(salon.getOwnerId());
        salonDto.setOpenTime(salon.getOpenTime());
        salonDto.setCloseTime(salon.getCloseTime());
        salonDto.setPhoneNumber(salon.getPhoneNumber());
        salonDto.setOwnerId(salon.getOwnerId());
        return salonDto;
    }
}

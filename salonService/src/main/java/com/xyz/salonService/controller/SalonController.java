package com.xyz.salonService.controller;

import com.xyz.salonService.mapper.SalonMapper;
import com.xyz.salonService.model.Salon;
import com.xyz.salonService.payload.dto.SalonDTO;
import com.xyz.salonService.payload.dto.UserDTO;
import com.xyz.salonService.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salons")
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;

    // http://localhost:5002/salons
    @PostMapping
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO){
        UserDTO userDTO=new UserDTO();
        userDTO.setId(1L);
        Salon salon =salonService.createSalon(salonDTO,userDTO);
        SalonDTO salonDTO1= SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(salonDTO1);

    }
    // http://localhost:5002/salons/{id}
    @PutMapping("/{salonId}")
    public ResponseEntity<SalonDTO> updateSalon(
            @PathVariable Long salonId,
            @RequestBody SalonDTO salonDTO

    )throws Exception{

        UserDTO userDTO=new UserDTO();
        userDTO.setId(1L);
        System.out.println("-------"+salonId+" email: "+salonDTO.getEmail());

        Salon salon =salonService.upadteSalon(salonDTO,userDTO,salonId);
        SalonDTO salonDTO1= SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(salonDTO1);


    }
    // http://localhost:5002/salons/{id}
    @GetMapping
    public ResponseEntity<List<SalonDTO>> getSalon() throws Exception{



        List<Salon> salons =salonService.getAllSalons();

        List<SalonDTO> salonDTOS=salons.stream().map((salon)->
        {
            SalonDTO salonDTO=SalonMapper.mapToDTO(salon);
            return salonDTO;
        }
        ).toList();
        return ResponseEntity.ok(salonDTOS);

    }
    @GetMapping("/{salonId}")
    public ResponseEntity<SalonDTO> getSalonById(
            @PathVariable Long salonId) throws Exception{



        Salon salon =salonService.getSalonById(salonId);

        SalonDTO salonDTO=SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(salonDTO);

    }


    @GetMapping("/search")
    public ResponseEntity<List<SalonDTO>> searchSalon(
            @RequestParam("city") String city) throws Exception{
        List<Salon> salons =salonService.searchSalonByCity(city);

        List<SalonDTO> salonDTOS=salons.stream().map((salon)->
                {
                    SalonDTO salonDTO=SalonMapper.mapToDTO(salon);
                    return salonDTO;
                }
        ).toList();
        return ResponseEntity.ok(salonDTOS);

    }
    @GetMapping("/owner")
    public ResponseEntity<SalonDTO> getSalonByOwnerId(
          //  @PathVariable Long salonId
    ) throws Exception{

        UserDTO userDTO=new UserDTO();
        userDTO.setId(1L);
        Salon salon =salonService.getSalonByOwnerId(userDTO.getId());

        SalonDTO salonDTO=SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(salonDTO);

    }


}

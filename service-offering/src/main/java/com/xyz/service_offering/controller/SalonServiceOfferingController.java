package com.xyz.service_offering.controller;

import com.xyz.service_offering.dto.CategoryDTO;
import com.xyz.service_offering.dto.SalonDTO;
import com.xyz.service_offering.dto.ServiceDTO;
import com.xyz.service_offering.model.ServiceOffering;
import com.xyz.service_offering.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/service-offering/salon-owner")
@RequiredArgsConstructor
public class SalonServiceOfferingController {
    private final ServiceOfferingService serviceOfferingService;

    @PostMapping
    public ResponseEntity<ServiceOffering> createService(
            @RequestBody ServiceDTO serviceDTO

            ){

        SalonDTO salonDTO=new SalonDTO();
        salonDTO.setId(1L);

        CategoryDTO categoryDTO=new CategoryDTO();
        categoryDTO.setId(serviceDTO.getCategory());
            ServiceOffering serviceOfferings=serviceOfferingService
                    .createService(salonDTO, serviceDTO, categoryDTO);
            return ResponseEntity.ok(serviceOfferings);


        }
    @PostMapping("/{id}")
    public ResponseEntity<ServiceOffering> updateService(
           @PathVariable Long id,
           @RequestBody ServiceOffering serviceOffering

    ) throws Exception {

        ServiceOffering serviceOfferings=serviceOfferingService
                .updateService(id, serviceOffering);
        return ResponseEntity.ok(serviceOfferings);


    }

    }

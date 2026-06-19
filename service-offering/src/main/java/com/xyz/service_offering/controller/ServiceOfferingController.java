package com.xyz.service_offering.controller;

import com.xyz.service_offering.model.ServiceOffering;
import com.xyz.service_offering.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/service-offering")
@RequiredArgsConstructor
public class ServiceOfferingController {
    private final ServiceOfferingService serviceOfferingService;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<Set<ServiceOffering>>  getServiceBySalonId(
            @PathVariable Long salonId,
            @RequestParam(required = false) Long categoryId
    ){
        Set<ServiceOffering> serviceOfferings=serviceOfferingService
                .getAllServiceBySalonId(salonId,categoryId);
        return ResponseEntity.ok(serviceOfferings);


    }

    @GetMapping("/{id}")
    public ResponseEntity < ServiceOffering > getServiceById(
            @PathVariable Long id
    )throws Exception{
        ServiceOffering serviceOfferings=serviceOfferingService
                .getServiceById(id);
        return ResponseEntity.ok(serviceOfferings);


    }
    @GetMapping("/list/{ids}")
    public ResponseEntity<Set<ServiceOffering>>  getServiceByIds(
            @PathVariable Set<Long> ids
    ){
        Set<ServiceOffering> serviceOfferings=serviceOfferingService
                .getServiceByIds(ids);
        return ResponseEntity.ok(serviceOfferings);


    }

}

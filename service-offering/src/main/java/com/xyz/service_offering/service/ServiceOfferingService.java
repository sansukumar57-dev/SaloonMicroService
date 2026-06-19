package com.xyz.service_offering.service;

import com.xyz.service_offering.dto.CategoryDTO;
import com.xyz.service_offering.dto.SalonDTO;
import com.xyz.service_offering.dto.ServiceDTO;
import com.xyz.service_offering.model.ServiceOffering;
import jakarta.persistence.SecondaryTable;

import java.util.List;
import java.util.Set;

public interface ServiceOfferingService {
    ServiceOffering createService(SalonDTO salonDTO,
                                  ServiceDTO serviceDTO,
                                  CategoryDTO categoryDTO);
    ServiceOffering updateService(Long serviceId,ServiceOffering service) throws Exception;

    Set<ServiceOffering> getAllServiceBySalonId(Long salonId,Long categoryId);

    Set<ServiceOffering> getServiceByIds(Set<Long> ids);

    ServiceOffering getServiceById(Long id) throws Exception;
}

package com.xyz.service_offering.service.Impl;

import com.xyz.service_offering.dto.CategoryDTO;
import com.xyz.service_offering.dto.SalonDTO;
import com.xyz.service_offering.dto.ServiceDTO;
import com.xyz.service_offering.model.ServiceOffering;
import com.xyz.service_offering.repository.ServiceOfferingRepository;
import com.xyz.service_offering.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl  implements ServiceOfferingService {

private final ServiceOfferingRepository serviceOfferingRepository;
   // private final ServiceOfferingService serviceOfferingService;

    @Override
    public ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO) {

        ServiceOffering serviceOffering = new ServiceOffering();
        serviceOffering.setImage(serviceDTO.getImage());
        serviceOffering.setName(serviceDTO.getName());
        serviceOffering.setDescription(serviceDTO.getDescription());
        serviceOffering.setPrice(serviceDTO.getPrice());
        serviceOffering.setDuration(serviceDTO.getDuration());
        serviceOffering.setSalonId(salonDTO.getId());
        serviceOffering.setCategoryId(categoryDTO.getId());
        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering updateService(Long serviceId, ServiceOffering service) throws Exception {
     ServiceOffering serviceOffering=serviceOfferingRepository.findById(serviceId).orElse(null)
;      if(serviceOffering == null){
            throw new Exception("Service not exist with id "+serviceId);
        }
        serviceOffering.setImage(service.getImage());
        serviceOffering.setName(service.getName());
        serviceOffering.setDescription(service.getDescription());
        serviceOffering.setPrice(service.getPrice());
        serviceOffering.setDuration(service.getDuration());

return serviceOfferingRepository.save(serviceOffering);


    }

    @Override
    public Set<ServiceOffering> getAllServiceBySalonId(Long salonId, Long categoryId) {
        Set<ServiceOffering> services = serviceOfferingRepository.findBySalonId(salonId);
        if(categoryId!=null){
            services=services.stream().filter((service)-> service.getCategoryId()!=null&&
                    service.getCategoryId().equals(categoryId)).collect(Collectors.toSet());
        }
        return services;
    }



    @Override
    public Set<ServiceOffering> getServiceByIds(Set<Long> ids) {
        List<ServiceOffering> services=serviceOfferingRepository.findAllById(ids);
        return new HashSet<>(services);
    }

    @Override
    public ServiceOffering getServiceById(Long id) throws Exception {
        ServiceOffering serviceOffering=serviceOfferingRepository.findById(id).orElse(null)
                ;      if(serviceOffering == null){
            throw new Exception("Service not exist with id "+id);
        }
        return serviceOffering;
    }
}

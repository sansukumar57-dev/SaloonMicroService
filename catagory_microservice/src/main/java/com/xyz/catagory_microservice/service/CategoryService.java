package com.xyz.catagory_microservice.service;

import com.xyz.catagory_microservice.dto.SalonDTO;
import com.xyz.catagory_microservice.model.Category;

import java.util.Set;

public interface CategoryService {

    Category saveCategory(Category category, SalonDTO salonDTO);

    //List<Category> getAllCategoryBySalon(Long id);
    Set<Category> getAllCategoryBySalonId(Long id);
    Category getCategoryById(Long id) throws Exception;
    void deleteCategoryById(Long id,Long salonId) throws Exception;
}

package com.xyz.catagory_microservice.controller;

import com.xyz.catagory_microservice.dto.SalonDTO;
import com.xyz.catagory_microservice.model.Category;
import com.xyz.catagory_microservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories/salon-owner")
public class SalonCategoryController {

    private  final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<Category>  createCategory(
           @RequestBody Category category
    ){
        SalonDTO salonDTO=new SalonDTO();
        salonDTO.setId(1L);

        Category saveCategories = categoryService.saveCategory(category,salonDTO);

        return ResponseEntity.ok(saveCategories);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String>  deleteCategory(
            @PathVariable Long id
    )throws Exception{
        SalonDTO salonDTO=new SalonDTO();
        salonDTO.setId(1L);

       categoryService.deleteCategoryById(id,salonDTO.getId());

        return ResponseEntity.ok("Category deleted successfully");
    }

}

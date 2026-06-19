package com.xyz.catagory_microservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HomeController {
    @GetMapping
    public String HomeControllerHandler(){
        return "Catagory Service microservice is running";
    }

}

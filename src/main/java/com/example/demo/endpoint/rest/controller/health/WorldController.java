package com.example.demo.endpoint.rest.controller.health;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class WorldController {
    public String  hello() {
        return "...hello world ";
    }
}

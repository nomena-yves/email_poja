package com.example.demo.endpoint.rest.controller.health;

import com.example.demo.Services.HelloWorldService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/hello")
public class WorldController {
  private final HelloWorldService service;

  @GetMapping
  @SneakyThrows
  public String helloWorld(@RequestParam String name) {
    return service.uploadHelloWorldMessage(name);
  }
}

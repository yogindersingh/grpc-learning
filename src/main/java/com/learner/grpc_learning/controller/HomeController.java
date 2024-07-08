package com.learner.grpc_learning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @GetMapping(path = "/home")
  String home(){
    return "Hello World";
  }
}

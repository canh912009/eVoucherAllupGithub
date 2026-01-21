package com.evoucher.partner.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/hello-world")
    String helloWorld() {
        return "Hello World";
    }

}

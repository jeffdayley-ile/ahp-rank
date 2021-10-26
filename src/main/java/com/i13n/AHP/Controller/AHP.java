package com.i13n.AHP.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AHP {
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}

package dev.faruk.sale.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaleController {
    @GetMapping("/")
    public String hello() {
        return "Hello, Sale!";
    }
}

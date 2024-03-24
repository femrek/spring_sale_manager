package dev.faruk.auth.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accessibility")
public class EndpointSecurityController {
    @GetMapping("/**")
    public AppSuccessResponse<Object> checkAccessibility() {
        return new AppSuccessResponse<>(null);
    }
}

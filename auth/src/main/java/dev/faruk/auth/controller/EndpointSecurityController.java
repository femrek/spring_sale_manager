package dev.faruk.auth.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/accessibility")
public class EndpointSecurityController {
    @GetMapping("/**")
    public ResponseEntity<AppSuccessResponse<Object>> checkAccessibility() {
        return new AppSuccessResponse<>(null).toResponseEntity();
    }
}

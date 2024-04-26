package dev.faruk.auth.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.logging.IgnoreLog;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accessibility")
public class EndpointSecurityController {
    @IgnoreLog
    @GetMapping("/**")
    public AppSuccessResponse<?> checkAccessibility() {
        return new AppSuccessResponse<>(null);
    }
}

package dev.faruk.auth.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dbLogging.IgnoreDbLog;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accessibility")
public class EndpointSecurityController {
    @IgnoreDbLog
    @GetMapping("/**")
    public AppSuccessResponse<?> checkAccessibility() {
        return new AppSuccessResponse<>(null);
    }
}

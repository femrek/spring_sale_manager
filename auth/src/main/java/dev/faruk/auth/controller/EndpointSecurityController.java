package dev.faruk.auth.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dbLogging.IgnoreDbLog;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/accessibility")
public class EndpointSecurityController {
    @IgnoreDbLog
    @GetMapping("/**")
    public AppSuccessResponse<?> checkAccessibility() {
        return new AppSuccessResponse<>(null);
    }
}

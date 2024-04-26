package dev.faruk.logging.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.LogDTO;
import dev.faruk.commoncodebase.logging.IgnoreLog;
import dev.faruk.logging.service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logging")
public class LoggingController {
    private final LoggingService loggingService;

    @Autowired
    public LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @IgnoreLog
    @GetMapping({"/", ""})
    public AppSuccessResponse<List<LogDTO>> showLogs(@RequestParam(name = "p", required = false) Integer page,
                                                     @RequestParam(name = "s", required = false) Integer size) {
        List<LogDTO> logs = loggingService.getLogs(page, size);
        return new AppSuccessResponse<>("%d logs are listed successfully".formatted(logs.size()), logs);
    }

    @IgnoreLog
    @GetMapping("/{id}")
    public AppSuccessResponse<LogDTO> showLog(@PathVariable Long id) {
        LogDTO log = loggingService.getLog(id);
        return new AppSuccessResponse<>("Log is listed successfully", log);
    }
}

package dev.faruk.logging.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.LogDTO;
import dev.faruk.commoncodebase.dbLogging.IgnoreDbLog;
import dev.faruk.logging.service.LoggingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/logging")
public class LoggingController {
    private final LoggingService loggingService;

    @Autowired
    public LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @IgnoreDbLog
    @GetMapping({"/", ""})
    public AppSuccessResponse<List<LogDTO>> showLogs(@RequestParam(name = "p", required = false) Integer page,
                                                     @RequestParam(name = "s", required = false) Integer size) {
        List<LogDTO> logs = loggingService.getLogs(page, size);
        return new AppSuccessResponse<>("%d logs are listed successfully".formatted(logs.size()), logs);
    }

    @IgnoreDbLog
    @GetMapping("/{id}")
    public AppSuccessResponse<LogDTO> showLog(@PathVariable Long id) {
        LogDTO log = loggingService.getLog(id);
        return new AppSuccessResponse<>("Log is listed successfully", log);
    }
}

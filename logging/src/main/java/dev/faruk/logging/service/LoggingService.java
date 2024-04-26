package dev.faruk.logging.service;

import dev.faruk.commoncodebase.dto.LogDTO;
import dev.faruk.commoncodebase.entity.AppLog;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.repository.base.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for showing logs
 */
@Service
public class LoggingService {
    private final LogRepository logRepository;

    @Autowired
    public LoggingService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    /**
     * Get logs with pagination.
     * @param page the page number. It starts from 1.
     * @param size the size of the page.
     * @return List of {@link LogDTO} objects. If there is no log, it returns an empty list. if page or size is not
     * valid, it throws an exception.
     */
    public List<LogDTO> getLogs(Integer page, Integer size) {
        if (page == null && size == null) {
            throw new AppHttpError.BadRequest("Page number and size are required");
        }
        if (page == null) {
            throw new AppHttpError.BadRequest("Page number is required");
        }
        if (size == null) {
            throw new AppHttpError.BadRequest("Size is required");
        }
        if (page < 1) {
            throw new AppHttpError.BadRequest("Page number must be greater than 0");
        }
        if (size < 1) {
            throw new AppHttpError.BadRequest("Size must be greater than 0");
        }

        List<AppLog> logs = logRepository.findAll(page, size);
        return logs.stream().map(LogDTO::new).toList();
    }

    /**
     * Get a log by id.
     * @param id the id of the log.
     * @return the {@link LogDTO} object of the log.
     */
    public LogDTO getLog(Long id) {
        AppLog log = logRepository.findById(id);
        return new LogDTO(log);
    }
}

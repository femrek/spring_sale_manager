package dev.faruk.commoncodebase.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.faruk.commoncodebase.constant.AppConstants;
import dev.faruk.commoncodebase.dto.log.AppHttpErrorLogCreateDTO;
import dev.faruk.commoncodebase.dto.log.AuthErrorLogCreateDTO;
import dev.faruk.commoncodebase.dto.log.ErrorLogCreateDTO;
import dev.faruk.commoncodebase.dto.log.SuccessLogCreateDTO;
import dev.faruk.commoncodebase.entity.AppLog;
import dev.faruk.commoncodebase.repository.base.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class LogService {
    private final LogRepository logRepository;
    private final AppConstants appConstants;

    @Autowired
    public LogService(LogRepository logRepository,
                      AppConstants appConstants) {
        this.logRepository = logRepository;
        this.appConstants = appConstants;
    }

    @Async
    public void saveLog(SuccessLogCreateDTO log) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String requestBodyJson = log.getRequest().getRequestBody() == null
                ? null : ow.writeValueAsString(log.getRequest().getRequestBody());
        String responseJson = ow.writeValueAsString(log.getResponse());

        AppLog appLog = AppLog.builder()
                .url(log.getRequest().getUrl())
                .method(log.getRequest().getMethod())
                .statusCode(log.getStatusCode())
                .requestBody(requestBodyJson)
                .response(responseJson)
                .moduleName(appConstants.moduleName)
                .requestAt(log.getRequest().getCreatedAt())
                .responseAt(log.getResponseTime())
                .build();

        logRepository.save(appLog);
    }

    @Async
    public void saveLog(AppHttpErrorLogCreateDTO log) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String requestBodyJson = log.getRequest().getRequestBody() == null
                ? null : ow.writeValueAsString(log.getRequest().getRequestBody());

        AppLog appLog = AppLog.builder()
                .requestBody(requestBodyJson)
                .statusCode(log.getError().getStatusCode().value())
                .error(log.getError().toString())
                .moduleName(appConstants.moduleName)
                .requestAt(log.getRequest().getCreatedAt())
                .responseAt(log.getResponseTime())
                .build();

        logRepository.save(appLog);
    }

    @Async
    public void saveLog(AuthErrorLogCreateDTO log) {
        AppLog appLog = AppLog.builder()
                .url(log.getUrl())
                .method(log.getMethod())
                .statusCode(log.getStatusCode())
                .error(log.getError())
                .moduleName(appConstants.moduleName)
                .responseAt(log.getResponseTime())
                .build();

        logRepository.save(appLog);
    }

    @Async
    public void saveLog(ErrorLogCreateDTO log) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String requestBodyJson = log.getRequest().getRequestBody() == null
                ? null : ow.writeValueAsString(log.getRequest().getRequestBody());
        String stackTraceJson = ow.writeValueAsString(log.getStackTrace());

        AppLog appLog = AppLog.builder()
                .requestBody(requestBodyJson)
                .statusCode(500)
                .error(log.getError())
                .stackTrace(stackTraceJson)
                .moduleName(appConstants.moduleName)
                .requestAt(log.getRequest().getCreatedAt())
                .responseAt(log.getResponseTime())
                .build();

        logRepository.save(appLog);
    }
}

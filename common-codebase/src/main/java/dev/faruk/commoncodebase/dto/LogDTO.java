package dev.faruk.commoncodebase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.faruk.commoncodebase.entity.AppLog;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogDTO {
    private Long id;
    private Integer statusCode;
    private String request;
    private String response;
    private String error;
    private String stackTrace;
    private Date requestAt;
    private Date responseAt;

    public LogDTO(AppLog appLog) {
        this.id = appLog.getId();
        this.statusCode = appLog.getStatusCode();
        this.request = appLog.getRequestBody();
        this.response = appLog.getResponse();
        this.error = appLog.getError();
        this.stackTrace = appLog.getStackTrace();
        this.requestAt = appLog.getRequestAt();
        this.responseAt = appLog.getResponseAt();
    }
}
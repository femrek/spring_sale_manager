package dev.faruk.commoncodebase.dto.log;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ErrorLogCreateDTO {
    private LogRequestDTO request;
    private String error;
    private StackTraceElement[] stackTrace;
    private Date responseTime;
}

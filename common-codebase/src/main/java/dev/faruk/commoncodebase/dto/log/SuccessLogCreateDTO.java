package dev.faruk.commoncodebase.dto.log;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SuccessLogCreateDTO {
    private LogRequestDTO request;
    private Integer statusCode;
    private Object response;
    private Date responseTime;
}

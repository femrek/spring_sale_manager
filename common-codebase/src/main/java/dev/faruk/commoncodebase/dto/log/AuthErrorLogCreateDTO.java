package dev.faruk.commoncodebase.dto.log;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AuthErrorLogCreateDTO {
    private String url;
    private String method;
    private Integer statusCode;
    private String error;
    private Date responseTime;
}

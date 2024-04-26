package dev.faruk.commoncodebase.dto.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LogRequestDTO {
    private String url;
    private String method;
    private Object requestBody;
    private Date createdAt;

    static public LogRequestDTO fromContext(Object body) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        final String method = request.getMethod();
        final String url = request.getRequestURL().toString();
        final String getParams = request.getQueryString();
        return LogRequestDTO.builder()
                .url(url + (getParams != null ? "?" + getParams : ""))
                .method(method)
                .requestBody(body)
                .createdAt(new Date())
                .build();
    }
}

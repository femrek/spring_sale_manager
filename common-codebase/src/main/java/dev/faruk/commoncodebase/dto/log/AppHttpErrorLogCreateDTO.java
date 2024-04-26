package dev.faruk.commoncodebase.dto.log;

import dev.faruk.commoncodebase.error.AppHttpError;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AppHttpErrorLogCreateDTO {
    private LogRequestDTO request;
    private AppHttpError error;
    private Date responseTime;
}

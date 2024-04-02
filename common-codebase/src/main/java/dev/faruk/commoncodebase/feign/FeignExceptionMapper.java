package dev.faruk.commoncodebase.feign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.faruk.commoncodebase.aspect.GlobalRestExceptionHandler;
import dev.faruk.commoncodebase.error.AppHttpError;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * FeignExceptionMapper is class for turning the defined FeignException into AppHttpError.
 */
@Component
public final class FeignExceptionMapper {
    /**
     * Map the FeignException to AppHttpError. throw the result of this method response to the client via
     * {@link GlobalRestExceptionHandler}.
     *
     * @param e the exception thrown by Feign
     * @return the AppHttpError
     */
    public AppHttpError map(FeignException e) {
        String message = getErrorMessage(e);
        return AppHttpError.byStatusCode(HttpStatus.valueOf(e.status()), message);
    }

    /**
     * Extract the error message from the FeignException
     *
     * @param e the exception
     * @return the error message
     */
    private String getErrorMessage(FeignException e) {
        ObjectMapper objectMapper = new ObjectMapper();
        String message = e.contentUTF8();
        try {
            Map<String, Object> errorContent = objectMapper.readValue(e.contentUTF8(), new TypeReference<>() {
            });
            if (errorContent.containsKey("message")) {
                if (errorContent.get("message") instanceof String) {
                    message = (String) errorContent.get("message");
                }
            }
        } catch (IOException ignored) {
        }
        return message;
    }
}

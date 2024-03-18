package dev.faruk.usermanagement.feign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.faruk.commoncodebase.error.AppHttpError;
import feign.FeignException;
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
     * {@link dev.faruk.commoncodebase.error.GlobalRestExceptionHandler}.
     *
     * @param e the exception thrown by Feign
     * @return the AppHttpError
     */
    public AppHttpError map(FeignException e) {
        String message = getErrorMessage(e);
        return switch (e.status()) {
            case 400 -> new AppHttpError.BadRequest(message);
            case 401 -> new AppHttpError.Unauthorized(message);
            case 403 -> new AppHttpError.Forbidden(message);
            case 404 -> new AppHttpError.NotFound(message);
            default -> new AppHttpError.InternalServerError(message);
        };
    }

    /**
     * Extract the error message from the FeignException
     *
     * @param e the exception
     * @return the error message
     */
    private String getErrorMessage(FeignException e) {
        System.out.println("e.contentUTF8(): " + e.contentUTF8());
        System.out.println("e.getMessage(): " + e.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        String message = e.contentUTF8();
        try {
            Map<String, Object> errorContent = objectMapper.readValue(e.contentUTF8(), new TypeReference<>() {
            });
            System.out.println("errorContent: " + errorContent);
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

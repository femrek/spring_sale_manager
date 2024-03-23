package dev.faruk.commoncodebase.feign;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * This is a Feign client for validate permission for the requests.
 */
@FeignClient(name = "identifyClient")
public interface IdentifyClient {
    @RequestLine(value = "GET /auth/accessibility{path}")
    @Headers({"Authorization: {authHeader}"})
    void checkAccessibility(@Param("authHeader") String token,
                            @Param("path") String path);
}

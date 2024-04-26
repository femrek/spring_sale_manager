package dev.faruk.sale.feign;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.UserDTO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "userClient")
public interface UserClient {
    @RequestLine(value = "GET /api/v1/auth/user")
    @Headers({"Authorization: {authHeader}"})
    AppSuccessResponse<UserDTO> getUser(@Param("authHeader") String authHeader);
}

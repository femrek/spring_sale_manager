package dev.faruk.auth.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.faruk.auth.service.AppUserDetailService;
import dev.faruk.auth.dto.AppUserDetails;
import dev.faruk.auth.service.AuthService;
import dev.faruk.commoncodebase.dto.log.AuthErrorLogCreateDTO;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.logging.LogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

/**
 * This class is used to filter the requests and authenticate the user by token. used for auth module only.
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private final AppUserDetailService appUserDetailService;
    private final AuthService authService;
    private final LogService logService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthenticationFilter(AppUserDetailService appUserDetailService,
                                AuthService authService,
                                LogService logService,
                                ObjectMapper objectMapper) {
        this.appUserDetailService = appUserDetailService;
        this.authService = authService;
        this.logService = logService;
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                final String username = authService.getUserByToken(authHeader).getUsername();
                AppUserDetails userDetails = appUserDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                final SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
                filterChain.doFilter(request, response);
                logger.info("auth user: " + username);
                logger.info("authorities: " + userDetails.getAuthorities());
                logger.info("response: " + response);
            } catch (AppHttpError e) {
                logger.error("Error occurred while authenticating the user: " + e.getMessage());
                String body = objectMapper.writeValueAsString(e.toJson());

                // log if the error about auth.
                if (e instanceof AppHttpError.Unauthorized || e instanceof AppHttpError.Forbidden) {
                    logService.saveLog(AuthErrorLogCreateDTO.builder()
                            .url(request.getRequestURL().toString())
                            .method(request.getMethod())
                            .statusCode(e.getStatusCode().value())
                            .error(body)
                            .responseTime(new Date())
                            .build());
                }

                response.setStatus(e.getStatusCode().value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.setStatus(e.getStatusCode().value());
                response.getWriter().write(body);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}

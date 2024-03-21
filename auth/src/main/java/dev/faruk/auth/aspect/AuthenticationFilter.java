package dev.faruk.auth.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.faruk.auth.config.AppUserDetailService;
import dev.faruk.auth.config.AppUserDetails;
import dev.faruk.auth.service.AuthService;
import dev.faruk.commoncodebase.error.AppHttpError;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * This class is used to filter the requests and authenticate the user by token. used for auth module only.
 */
@Component
@Order(HIGHEST_PRECEDENCE)
public class AuthenticationFilter extends OncePerRequestFilter {
    private final AppUserDetailService appUserDetailService;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthenticationFilter(AppUserDetailService appUserDetailService,
                                AuthService authService,
                                ObjectMapper objectMapper) {
        this.appUserDetailService = appUserDetailService;
        this.authService = authService;
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
                response.setStatus(e.getStatusCode().value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.setStatus(e.getStatusCode().value());
                response.getWriter().write(objectMapper.writeValueAsString(e.toJson()));
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
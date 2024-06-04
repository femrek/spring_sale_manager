package dev.faruk.auth.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.faruk.auth.service.AppUserDetailService;
import dev.faruk.auth.dto.AppUserDetails;
import dev.faruk.auth.service.AuthService;
import dev.faruk.commoncodebase.dto.log.AuthErrorLogCreateDTO;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.dbLogging.DbLoggingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

/**
 * This class is used to filter the requests and authenticate the user by token. used for auth module only.
 */
@Log4j2
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private final AppUserDetailService appUserDetailService;
    private final AuthService authService;
    private final DbLoggingService dbLoggingService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthenticationFilter(AppUserDetailService appUserDetailService,
                                AuthService authService,
                                DbLoggingService dbLoggingService,
                                ObjectMapper objectMapper) {
        this.appUserDetailService = appUserDetailService;
        this.authService = authService;
        this.dbLoggingService = dbLoggingService;
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
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                final SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
                filterChain.doFilter(request, response);
            } catch (AppHttpError e) {
                log.debug("Authentication error on before filter", e);
                String body = objectMapper.writeValueAsString(e.toJson());

                // log if the error about auth.
                if (e instanceof AppHttpError.Unauthorized || e instanceof AppHttpError.Forbidden) {
                    dbLoggingService.saveLog(AuthErrorLogCreateDTO.builder()
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
            } catch (UsernameNotFoundException e) {
                log.error("the username from the token does not exist in the database.", e);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}

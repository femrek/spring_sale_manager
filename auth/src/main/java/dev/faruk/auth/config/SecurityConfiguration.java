package dev.faruk.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.faruk.auth.aspect.AuthenticationFilter;
import dev.faruk.auth.service.AppUserDetailService;
import dev.faruk.auth.service.AuthService;
import dev.faruk.commoncodebase.logging.LogService;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public AuthenticationProvider authenticationProvider(UserRepository userRepository) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(appUserDetailService(userRepository));
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AppUserDetailService appUserDetailService(UserRepository userRepository) {
        return new AppUserDetailService(userRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           UserRepository userRepository,
                                           AuthService authService,
                                           LogService logService,
                                           ObjectMapper objectMapper) throws Exception {
        // specify authorization of the end-points
        httpSecurity.authorizeHttpRequests(configurer -> configurer
                // auth end-points
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/user").authenticated()
                .requestMatchers("/user-management/**").hasAuthority("ADMIN")

                // end-points of other services
                .requestMatchers("/accessibility/api/v1/product/**").permitAll()
                .requestMatchers("/accessibility/api/v1/sale/**").hasAuthority("CASHIER")
                .requestMatchers("/accessibility/api/v1/offer/**").hasAuthority("CASHIER")
                .requestMatchers("/accessibility/api/v1/report/**").hasAuthority("MANAGER")
                .requestMatchers("/accessibility/api/v1/user/**").hasAuthority("ADMIN")
                .requestMatchers("/accessibility/api/v1/logging/**").permitAll()

                // always deny undefined end-points
                .anyRequest().denyAll()
        );

        // return specified error response if an authentication or authorization error.
        httpSecurity.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.defaultAccessDeniedHandlerFor(
                            accessDeniedHandler,
                            (request) -> false
                    );
                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(
                            accessDeniedHandler
                    );
                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
                            authenticationEntryPoint
                    );
                    httpSecurityExceptionHandlingConfigurer.defaultAuthenticationEntryPointFor(
                            authenticationEntryPoint,
                            (request) -> false
                    );
                }
        );

        // other configurations
        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.addFilterBefore(new AuthenticationFilter(
                appUserDetailService(userRepository),
                authService,
                logService,
                objectMapper), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    private final AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
        // determine the status code and message
        SecurityContext context = SecurityContextHolder.getContext();
        boolean isAuthed = context.getAuthentication().isAuthenticated();
        int status = isAuthed ? 403 : 401;
        String message = isAuthed ? "Forbidden" : "Unauthorized";

        // set the status code and message of response
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"status\":%d,\"message\":\"%s\"}", status, message));
    };

    private final AuthenticationEntryPoint authenticationEntryPoint = (request, response, authException) -> {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":401, \"message\":\"Unauthorized, no authentication token.\"}");
        }
    };
}

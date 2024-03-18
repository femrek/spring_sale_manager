package dev.faruk.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.faruk.auth.aspect.AuthenticationFilter;
import dev.faruk.auth.service.AuthService;
import dev.faruk.commoncodebase.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.SecurityFilterChain;
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
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           UserRepository userRepository,
                                           AuthService authService,
                                           ObjectMapper objectMapper) throws Exception {
        // specify authorization of the end-points
        httpSecurity.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/user").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/user").hasAuthority("MANAGER")
                .requestMatchers(HttpMethod.GET, "/user").hasAuthority("CASHIER")
                .requestMatchers(HttpMethod.POST, "/logout").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/logout").hasAuthority("MANAGER")
                .requestMatchers(HttpMethod.POST, "/logout").hasAuthority("CASHIER")
                .requestMatchers(HttpMethod.POST, "/user-management/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/user-management/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/user-management/**").hasAuthority("ADMIN")
                .anyRequest().denyAll()
        );

        // return specified error response if an authentication or authorization error.
        httpSecurity.exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                httpSecurityExceptionHandlingConfigurer.defaultAccessDeniedHandlerFor(
                        (request, response, accessDeniedException) -> {
                            // determine the status code and message
                            SecurityContext context = SecurityContextHolder.getContext();
                            boolean isAuthed = context.getAuthentication().isAuthenticated();
                            int status = isAuthed ? 403 : 401;
                            String message = isAuthed ? "Forbidden" : "Unauthorized";

                            // set the status code and message of response
                            response.setStatus(status);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    String.format("{\"status\":%d,\"message\":\"%s\"}", status, message));
                        },
                        (request) -> false
                )
        );

        // other configurations
        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.addFilterBefore(new AuthenticationFilter(
                appUserDetailService(userRepository),
                authService,
                objectMapper), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AppUserDetailService appUserDetailService(UserRepository userRepository) {
        return new AppUserDetailService(userRepository);
    }
}

package dev.faruk.auth.config;

import dev.faruk.auth.dao.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.GET, "/auth/user").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/auth/user").hasAuthority("MANAGER")
                .requestMatchers(HttpMethod.GET, "/auth/user").hasAuthority("CASHIER")
                .requestMatchers(HttpMethod.POST, "/auth/logout").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/auth/logout").hasAuthority("MANAGER")
                .requestMatchers(HttpMethod.POST, "/auth/logout").hasAuthority("CASHIER")
                .requestMatchers(HttpMethod.POST, "/auth/register").hasAuthority("ADMIN")
                .anyRequest().permitAll()
        );
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

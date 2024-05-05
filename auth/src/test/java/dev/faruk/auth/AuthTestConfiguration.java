package dev.faruk.auth;

import dev.faruk.auth.constant.AuthConstants;
import dev.faruk.auth.service.*;
import dev.faruk.auth.controller.*;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import dev.faruk.commoncodebase.repository.testImplementations.UserRepositoryTestImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthTestConfiguration {
    @Bean("testAppUserDetailService")
    public AppUserDetailService appUserDetailService(@Qualifier("testUserRepository") UserRepository userRepository) {
        return new AppUserDetailService(userRepository);
    }

    @Bean("testAuthService")
    @Primary
    public AuthService authService(@Qualifier("testUserRepository") UserRepository userRepository,
                                   @Qualifier("testJwtService") JwtService jwtService,
                                   PasswordEncoder passwordEncoder) {
        return new AuthService(userRepository, passwordEncoder, jwtService);
    }

    @Bean("testJwtService")
    public JwtService jwtService(@Qualifier("testAuthConstants") AuthConstants jwtSecretService) {
        return new JwtService(jwtSecretService);
    }

    @Bean("testAuthConstants")
    public AuthConstants jwtSecretService() {
        return new AuthConstants(
                1000 * 60 * 60 * 2L,
                "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437");
    }

    @Bean("testUserManagementService")
    public UserManagementService userManagementService(@Qualifier("testUserRepository") UserRepository userRepository,
                                                       PasswordEncoder passwordEncoder) {
        return new UserManagementService(userRepository, passwordEncoder);
    }

    @Bean("testAuthController")
    public AuthController authController(@Qualifier("testAuthService") AuthService authService) {
        return new AuthController(authService);
    }

    @Bean("testUserRepository")
    public UserRepository userRepository() {
        return new UserRepositoryTestImpl();
    }

    @Bean("testPasswordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("authTestDataSource")
    public AuthTestDataSource testDataSource(@Qualifier("testUserRepository") UserRepository userRepository,
                                             @Qualifier("testPasswordEncoder") PasswordEncoder passwordEncoder) {
        return new AuthTestDataSource(userRepository, passwordEncoder);
    }
}

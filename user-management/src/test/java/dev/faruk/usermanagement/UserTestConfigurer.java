package dev.faruk.usermanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.faruk.commoncodebase.feign.FeignExceptionMapper;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import dev.faruk.commoncodebase.repository.testImplementations.UserRepositoryTestImpl;
import dev.faruk.usermanagement.feign.UserManagementClient;
import dev.faruk.usermanagement.service.UserRoleService;
import dev.faruk.usermanagement.service.UserService;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserTestConfigurer {
    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryTestImpl();
    }

    @Bean
    public FeignExceptionMapper feignExceptionMapper() {
        return new FeignExceptionMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public UserManagementClient userManagementClient(UserRepository userRepository,
                                                     ObjectMapper objectMapper,
                                                     PasswordEncoder passwordEncoder) {
        return Feign.builder()
                .client(new UserManagementTestClient(userRepository, objectMapper, passwordEncoder))
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(UserManagementClient.class, "http://localhost:8080");
    }

    @Bean
    public UserTestDataSource userTestDataSource(UserRepository userRepository,
                                                 PasswordEncoder passwordEncoder) {
        return new UserTestDataSource(userRepository, passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserService userService(UserRepository userRepository,
                                   FeignExceptionMapper feignExceptionMapper,
                                   UserManagementClient userManagementClient) {
        return new UserService(userRepository, feignExceptionMapper, userManagementClient);
    }

    @Bean
    public UserRoleService userRoleService(UserRepository userRepository) {
        return new UserRoleService(userRepository);
    }
}

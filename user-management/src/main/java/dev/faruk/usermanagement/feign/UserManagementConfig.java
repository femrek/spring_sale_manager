package dev.faruk.usermanagement.feign;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static dev.faruk.commoncodebase.constant.AppConstants.HOST;

@Configuration
public class UserManagementConfig {
    @Bean
    @Qualifier("userManagementClient")
    public UserManagementClient getUserManagementClient() {
        return Feign.builder()
                .client(new OkHttpClient())
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(UserManagementClient.class, HOST);
    }
}

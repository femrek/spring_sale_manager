package dev.faruk.usermanagement.feign;

import dev.faruk.commoncodebase.constant.AppConstants;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserManagementClientConfig {
    @Bean
    @Qualifier("userManagementClient")
    public UserManagementClient getUserManagementClient(AppConstants appConstants) {
        return Feign.builder()
                .client(new OkHttpClient())
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(UserManagementClient.class, appConstants.host);
    }
}

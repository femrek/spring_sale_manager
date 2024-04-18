package dev.faruk.commoncodebase.feign;

import dev.faruk.commoncodebase.constant.AppConstants;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorizeClientConfig {
    @Bean
    public AuthorizeClient authorizeClient(AppConstants appConstants) {
        return Feign.builder()
                .client(new OkHttpClient())
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(AuthorizeClient.class, appConstants.host);
    }
}

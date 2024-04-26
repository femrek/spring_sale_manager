package dev.faruk.commoncodebase.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {
    private static final String HOST_TEMPLATE = "http://%s:8080";

    @Value("${spring.application.name}")
    public String moduleName;

    public final String host;

    public AppConstants() {
        String hostEnv = System.getenv("GATEWAY_HOST");
        String hostName = hostEnv != null ? hostEnv : "localhost";
        this.host = String.format(HOST_TEMPLATE, hostName);
    }
}

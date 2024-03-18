package dev.faruk.usermanagement;

import dev.faruk.commoncodebase.error.GlobalRestExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages={"dev.faruk.usermanagement", "dev.faruk.commoncodebase"})
@EntityScan(basePackages="dev.faruk.commoncodebase.entity")
@Import(GlobalRestExceptionHandler.class)
@EnableFeignClients
public class UserManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}
}

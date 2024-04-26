package dev.faruk.logging;

import dev.faruk.commoncodebase.aspect.GlobalRestExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages={"dev.faruk.logging", "dev.faruk.commoncodebase"})
@EntityScan(basePackages="dev.faruk.commoncodebase.entity")
@Import(GlobalRestExceptionHandler.class)
public class LoggingApplication {
	public static void main(String[] args) {
		SpringApplication.run(LoggingApplication.class, args);
	}
}

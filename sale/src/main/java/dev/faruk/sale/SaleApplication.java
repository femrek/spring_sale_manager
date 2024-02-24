package dev.faruk.sale;

import dev.faruk.commoncodebase.error.GlobalRestExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages={"dev.faruk.sale", "dev.faruk.commoncodebase"})
@EntityScan(basePackages="dev.faruk.commoncodebase.entity")
@Import(GlobalRestExceptionHandler.class)
public class SaleApplication {
	public static void main(String[] args) {
		SpringApplication.run(SaleApplication.class, args);
	}
}

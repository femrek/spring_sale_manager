package dev.faruk.sale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages={"dev.faruk.sale", "dev.faruk.commoncodebase"})
@EntityScan(basePackages="dev.faruk.commoncodebase.entity")
public class SaleApplication {
	public static void main(String[] args) {
		SpringApplication.run(SaleApplication.class, args);
	}
}

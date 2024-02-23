package dev.faruk.sale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"dev.faruk.sale", "dev.faruk.commoncodebase"})
public class SaleApplication {
	public static void main(String[] args) {
		SpringApplication.run(SaleApplication.class, args);
	}
}

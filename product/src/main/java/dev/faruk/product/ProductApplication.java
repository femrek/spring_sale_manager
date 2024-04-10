package dev.faruk.product;

import dev.faruk.commoncodebase.aspect.GlobalRestExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages={"dev.faruk.product", "dev.faruk.commoncodebase"})
@EntityScan(basePackages="dev.faruk.commoncodebase.entity")
@Import(GlobalRestExceptionHandler.class)
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}

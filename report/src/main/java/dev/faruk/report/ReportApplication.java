package dev.faruk.report;

import dev.faruk.commoncodebase.error.GlobalRestExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages={"dev.faruk.report", "dev.faruk.commoncodebase"})
@EntityScan(basePackages="dev.faruk.commoncodebase.entity")
@Import(GlobalRestExceptionHandler.class)
public class ReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class, args);
    }
}

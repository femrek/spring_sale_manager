package dev.faruk.report;

import dev.faruk.commoncodebase.repository.base.SaleRepository;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import dev.faruk.report.service.ReportService;
import dev.faruk.commoncodebase.repository.testImplementations.SaleRepositoryTestImpl;
import dev.faruk.commoncodebase.repository.testImplementations.UserRepositoryTestImpl;
import org.springframework.context.annotation.Bean;

public class ReportTestConfigurer {
    @Bean("testReportService")
    public ReportService reportService(SaleRepository saleRepository) {
        return new ReportService(
                saleRepository,
                null
        );
    }

    @Bean("testSaleRepository")
    public SaleRepository saleRepository() {
        return new SaleRepositoryTestImpl();
    }

    @Bean("testUserRepository")
    public UserRepository userRepository() {
        return new UserRepositoryTestImpl();
    }
}

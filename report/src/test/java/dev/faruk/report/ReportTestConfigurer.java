package dev.faruk.report;

import dev.faruk.commoncodebase.repository.base.SaleRepository;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import dev.faruk.report.service.ReportService;
import dev.faruk.commoncodebase.repository.testImplementations.SaleTestRepository;
import dev.faruk.commoncodebase.repository.testImplementations.UserTestRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class ReportTestConfigurer {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean("testReportService")
    public ReportService reportService(@Qualifier("testSaleRepository") SaleRepository saleRepository) {
        return new ReportService(
                saleRepository,
                null
        );
    }

    @Bean("testSaleRepository")
    public SaleRepository saleRepository() {
        return new SaleTestRepository();
    }

    @Bean("testUserRepository")
    public UserRepository userRepository() {
        return new UserTestRepository();
    }
}

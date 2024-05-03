package dev.faruk.sale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.feign.FeignExceptionMapper;
import dev.faruk.commoncodebase.repository.base.OfferRepository;
import dev.faruk.commoncodebase.repository.base.ProductRepository;
import dev.faruk.commoncodebase.repository.base.SaleRepository;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import dev.faruk.commoncodebase.repository.testImplementations.OfferRepositoryTestImpl;
import dev.faruk.commoncodebase.repository.testImplementations.ProductRepositoryTestImpl;
import dev.faruk.commoncodebase.repository.testImplementations.SaleRepositoryTestImpl;
import dev.faruk.commoncodebase.repository.testImplementations.UserRepositoryTestImpl;
import dev.faruk.sale.datasource.SaleTestDataSource;
import dev.faruk.sale.feign.UserClient;
import dev.faruk.sale.service.OfferService;
import dev.faruk.sale.service.SaleService;
import feign.*;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class SaleTestConfigurations {
    @Bean("testSaleTestDataSource")
    public SaleTestDataSource saleTestDataSource(OfferRepository offerRepository,
                                                 SaleRepository saleRepository,
                                                 UserRepository userRepository,
                                                 ProductRepository productRepository) {
        return new SaleTestDataSource(offerRepository, saleRepository, userRepository, productRepository);
    }

    @Bean("testSaleService")
    public SaleService saleService(SaleRepository saleRepository,
                                   ProductRepository productRepository,
                                   UserRepository userRepository,
                                   UserClient userClient,
                                   FeignExceptionMapper feignExceptionMapper,
                                   OfferRepository offerRepository) {
        return new SaleService(
                saleRepository,
                productRepository,
                userRepository,
                userClient,
                feignExceptionMapper,
                offerRepository
        );
    }

    @Bean("testOfferService")
    public OfferService offerService(OfferRepository offerRepository) {
        return new OfferService(offerRepository);
    }

    @Bean("testUserClient")
    public UserClient userClient(@Qualifier("testUserRepository") UserRepository userRepository) {

        return Feign.builder()
                .client(new UserTestClient(userRepository, new ObjectMapper()))
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(UserClient.class, "http://localhost:8080");
    }

    @Bean("testFeignExceptionMapper")
    public FeignExceptionMapper feignExceptionMapper() {
        return new FeignExceptionMapper();
    }

    @Bean("testProductRepository")
    public ProductRepository productRepository() {
        return new ProductRepositoryTestImpl();
    }

    @Bean("testUserRepository")
    public UserRepository userRepository() {
        return new UserRepositoryTestImpl();
    }

    @Bean("testSaleRepository")
    public SaleRepository saleRepository() {
        return new SaleRepositoryTestImpl();
    }

    @Bean("testOfferRepository")
    public OfferRepository offerRepository() {
        return new OfferRepositoryTestImpl();
    }

    private record UserTestClient(UserRepository userRepository, ObjectMapper objectMapper) implements Client {
        @Override
        public Response execute(Request request, Request.Options options) throws JsonProcessingException {
            AppSuccessResponse<AppUser> body = new AppSuccessResponse<>(
                    "User found successfully",
                    userRepository.findByUsername(SaleTestDataSource.CASHIER_USERNAME_FOR_SALE_CREATION));
            String requestBodyJson = objectMapper.writeValueAsString(body.toJson());
            return Response.builder()
                    .status(200)
                    .reason("OK")
                    .request(request)
                    .body(requestBodyJson, StandardCharsets.UTF_8)
                    .headers(request.headers())
                    .build();
        }
    }
}


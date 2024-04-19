package dev.faruk.sale.service;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.SaleDTO;
import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.entity.*;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.feign.FeignExceptionMapper;
import dev.faruk.commoncodebase.repository.base.ProductRepository;
import dev.faruk.commoncodebase.repository.base.SaleRepository;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import dev.faruk.sale.dto.SalePostRequest;
import dev.faruk.sale.feign.UserClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserClient userClient;
    private final FeignExceptionMapper feignExceptionMapper;

    @Autowired
    public SaleService(SaleRepository saleRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository,
                       UserClient userClient,
                       FeignExceptionMapper feignExceptionMapper) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.userClient = userClient;
        this.feignExceptionMapper = feignExceptionMapper;
    }

    /**
     * This method creates a sale.
     *
     * @param salePostRequest the request body including the sale data
     * @param authHeader      the authorization header
     * @return the created and saved sale data
     */
    public SaleDTO create(SalePostRequest salePostRequest, String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            throw new AppHttpError.BadRequest("Authorization header is required");
        }

        // Fetches the user by the given auth header
        Long cashierId;
        try {
            AppSuccessResponse<UserDTO> requestSender = userClient.getUser(authHeader);
            if (requestSender.getData() == null) {
                throw new AppHttpError.InternalServerError("Auth user could not found");
            }
            cashierId = requestSender.getData().getId();
        } catch (FeignException e) {
            throw feignExceptionMapper.map(e);
        }
        AppUser cashier = userRepository.findOnlyExistById(cashierId);

        // Checks if the user is a cashier
        if (cashier == null) {
            throw new AppHttpError.BadRequest(String.format("Cashier not found with id, %d", cashierId));
        }
        if (!isCashier(cashier)) {
            throw new AppHttpError.BadRequest(
                    String.format("User with id %d (%s) is not a cashier", cashierId, cashier.getUsername()));
        }

        // Creates a sale with the given data
        Sale sale = Sale.builder()
                .receivedMoney(salePostRequest.getReceivedMoney())
                .cashier(cashier)
                .build();
        List<SalePostRequest.ProductDetails> productDetails = salePostRequest.getProducts();
        for (SalePostRequest.ProductDetails productDetail : productDetails) {
            final Product product = productRepository.findById(productDetail.getProductId());
            SaleProduct saleProduct = SaleProduct.builder()
                    .sale(sale)
                    .product(product)
                    .productCount(productDetail.getProductCount())
                    .unitPrice(product.getPrice())
                    .build();
            sale.add(saleProduct);
        }
        return new SaleDTO(saleRepository.create(sale));
    }

    /**
     * Fetches and turns the sale with the given id into a sale data transfer object and returns it.
     *
     * @param id the id of the sale to be shown
     * @return the sale with the given id
     */
    public SaleDTO findById(Long id) {
        Sale sale = saleRepository.findById(id);
        if (sale == null) return null;
        return new SaleDTO(sale);
    }

    /**
     * Deletes the sale with the given id from the database.
     *
     * @param id the id of the sale to be deleted
     */
    public void deletePermanentById(Long id) {
        final Sale saleWillBeRemoved = saleRepository.findById(id);
        if (saleWillBeRemoved == null) throw new RuntimeException(String.format("sale does not exist with id: %d", id));
        saleRepository.deletePermanent(saleWillBeRemoved);
    }

    /**
     * This method lists all sales saved in the database by the given cashier.
     *
     * @param user the user will be checked if it is a cashier
     * @return true if the user is a cashier, otherwise false
     */
    private boolean isCashier(AppUser user) {
        for (AppUserRole role : user.getRoles()) {
            if (role.getName().equalsIgnoreCase("CASHIER")) return true;
        }
        return false;
    }
}

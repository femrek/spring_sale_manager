package dev.faruk.sale.service;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.SaleDTO;
import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.entity.*;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.feign.FeignExceptionMapper;
import dev.faruk.commoncodebase.repository.base.OfferRepository;
import dev.faruk.commoncodebase.repository.base.ProductRepository;
import dev.faruk.commoncodebase.repository.base.SaleRepository;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import dev.faruk.sale.dto.SalePostRequest;
import dev.faruk.sale.feign.UserClient;
import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Log4j2
@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserClient userClient;
    private final FeignExceptionMapper feignExceptionMapper;
    private final OfferRepository offerRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository,
                       UserClient userClient,
                       FeignExceptionMapper feignExceptionMapper,
                       OfferRepository offerRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.userClient = userClient;
        this.feignExceptionMapper = feignExceptionMapper;
        this.offerRepository = offerRepository;
    }

    /**
     * This method creates a sale.
     *
     * @param salePostRequest the request body including the sale data
     * @param authHeader      the authorization header
     * @return the created and saved sale data
     */
    public SaleDTO create(SalePostRequest salePostRequest, String authHeader) {
        return new SaleDTO(saleRepository.create(_findTheCashierAndGenerateSale(salePostRequest, authHeader)));
    }

    /**
     * This method previews a sale. returns the preview of the sale data. It does not save the sale to the database.
     *
     * @param salePostRequest the request body including the sale data
     * @param authHeader      the authorization header
     * @return the preview of the sale data
     */
    public SaleDTO preview(SalePostRequest salePostRequest, String authHeader) {
        return new SaleDTO(_findTheCashierAndGenerateSale(salePostRequest, authHeader));
    }

    /**
     * Fetches the user by the given auth header and generates a sale with the given data.
     * @param salePostRequest the request body including the sale data
     * @param authHeader the authorization header
     * @return the generated sale
     */
    private Sale _findTheCashierAndGenerateSale(SalePostRequest salePostRequest, String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            throw new AppHttpError.BadRequest("Authorization header is required");
        }

        // Fetches the payment method by the given id
        PaymentMethod paymentMethod = _getPaymentMethod(salePostRequest);

        // Fetches the cashier by the given auth header
        AppUser cashier = _getCashier(authHeader);

        // Generates a sale with the given data
        Sale sale = _generateSale(salePostRequest, cashier, paymentMethod);

        // Checks if the received money is enough
        SaleDTO saleDTO = new SaleDTO(sale);
        if (saleDTO.getTotal() > sale.getReceivedMoney()) {
            throw new AppHttpError.BadRequest("Received money is not enough");
        }

        return sale;
    }

    /**
     * This method fetches the payment method by the given id.
     * @param salePostRequest the request body including the sale data
     * @return the payment method
     */
    private PaymentMethod _getPaymentMethod(SalePostRequest salePostRequest) {
        // check if the payment method is given
        if (salePostRequest.getPaymentMethodId() == null) {
            throw new AppHttpError.BadRequest("'PaymentMethodId' is required");
        }

        // fetch the payment method by the given id
        PaymentMethod paymentMethod = saleRepository.findPaymentMethodById(salePostRequest.getPaymentMethodId());

        // check if the payment method is found
        if (paymentMethod == null) {
            throw new AppHttpError
                    .BadRequest("Payment method not found with id, %d".formatted(salePostRequest.getPaymentMethodId()));
        }

        return paymentMethod;
    }

    /**
     * This method lists all sales saved in the database by the given cashier.
     *
     * @param user the user will be checked if it is a cashier
     * @return true if the user is a cashier, otherwise false
     */
    private boolean _isCashier(AppUser user) {
        for (AppUserRole role : user.getRoles()) {
            if (role.getName().equalsIgnoreCase("CASHIER")) return true;
        }
        return false;
    }

    /**
     * This method fetches the cashier by the given auth header.
     * @param authHeader the authorization header
     * @return entity object of the cashier
     */
    private AppUser _getCashier(String authHeader) {
        // Fetches the user by the given auth header
        Long cashierId;
        try {
            AppSuccessResponse<UserDTO> requestSender = userClient.getUser(authHeader);
            if (requestSender.getData() == null) {
                log.warn("Auth user could not found when trying to create sale.");
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
        if (!_isCashier(cashier)) {
            throw new AppHttpError.BadRequest(
                    String.format("User with id %d (%s) is not a cashier", cashierId, cashier.getUsername()));
        }

        return cashier;
    }

    /**
     * This method generates a sale with the given data.
     * @param salePostRequest the request body including the sale data
     * @param cashier the cashier who creates the sale
     * @return the generated sale
     */
    private Sale _generateSale(SalePostRequest salePostRequest, AppUser cashier, PaymentMethod paymentMethod) {
        Offer offer = null;
        if (salePostRequest.getOfferId() != null) {
            // Fetch the offer by the given offer id
            offer = offerRepository.findById(salePostRequest.getOfferId());

            // Check if the offers date is valid
            if (offer.getValidSince().after(new Timestamp(System.currentTimeMillis()))) {
                log.debug("Offer %s is not valid yet when generating sale.".formatted(offer.getName()));
                throw new AppHttpError.BadRequest("Offer %s is not valid yet".formatted(offer.getName()));
            }
            else if (offer.getValidUntil().before(new Timestamp(System.currentTimeMillis()))) {
                log.debug("Offer %s is expired when generating sale.".formatted(offer.getName()));
                throw new AppHttpError.BadRequest("Offer %s is expired".formatted(offer.getName()));
            }

            // Check if the offer is satisfied by the sale products
            if (!_doesOfferSatisfiedByProductList(salePostRequest.getProducts(), offer)) {
                log.debug("Offer %s is not satisfied by the sale when generating sale.".formatted(offer.getName()));
                throw new AppHttpError.BadRequest("Offer %s is not satisfied by the sale".formatted(offer.getName()));
            }
        }

        // Creates a sale with the given data
        Sale sale = Sale.builder()
                .receivedMoney(salePostRequest.getReceivedMoney())
                .cashier(cashier)
                .paymentMethod(paymentMethod)
                .offers(offer != null ? List.of(offer) : null)
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

        return sale;
    }

    /**
     * This method checks if the offer is satisfied by the sale products.
     * @param products the products in the sale
     * @param offer the offer to be checked
     * @return true if the offer is satisfied by the sale products, otherwise false
     */
    private boolean _doesOfferSatisfiedByProductList(List<SalePostRequest.ProductDetails> products, Offer offer) {
        if (offer.getRequiredProducts().size() > products.size()) return false;
        outer:
        for (OfferProduct offerProduct : offer.getRequiredProducts()) {
            for (SalePostRequest.ProductDetails product : products) {
                // when the product is found in the sale products
                if (offerProduct.getProduct().getId().equals(product.getProductId())) {
                    // check if the product count is enough
                    if (product.getProductCount() < offerProduct.getRequiredCount()) {
                        log.debug("Product %s count is not enough in the sale when checking offer(%s)."
                                .formatted(offerProduct.getProduct().getId(), offer.getId()));
                        return false;
                    }

                    continue outer;
                }
            }

            // when the product is not found in the sale products
            log.debug("Product %s is not found in the sale when checking offer."
                    .formatted(offerProduct.getProduct().getId()));
            return false;
        }
        return true;
    }
}

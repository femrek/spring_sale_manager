package dev.faruk.sale.service;

import dev.faruk.commoncodebase.dto.SaleDTO;
import dev.faruk.commoncodebase.entity.*;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.repository.ProductRepository;
import dev.faruk.commoncodebase.repository.SaleRepository;
import dev.faruk.commoncodebase.repository.UserRepository;
import dev.faruk.sale.dto.SalePostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * This method creates a sale.
     *
     * @param salePostRequest the request body including the sale data
     * @return the created and saved sale data
     */
    public SaleDTO create(SalePostRequest salePostRequest) {
        if (salePostRequest.getCashierId() == null) throw new AppHttpError.BadRequest("Cashier id is required");
        AppUser cashier = userRepository.findOnlyExistById(salePostRequest.getCashierId());
        if (cashier == null) {
            throw new AppHttpError.BadRequest(
                    String.format("Cashier not found with given id, %d", salePostRequest.getCashierId())
            );
        }
        if (!isCashier(cashier)) {
            throw new AppHttpError.BadRequest(
                    String.format("User with id %d (%s) is not a cashier", salePostRequest.getCashierId(), cashier.getUsername())
            );
        }
        Sale sale = new Sale(salePostRequest.getReceivedMoney(), cashier);
        List<SalePostRequest.ProductDetails> productDetails = salePostRequest.getProducts();
        for (SalePostRequest.ProductDetails productDetail : productDetails) {
            final Product product = productRepository.findById(productDetail.getProductId());
            SaleProduct saleProduct = new SaleProduct(sale, product, productDetail.getProductCount(), product.getPrice());
            sale.add(saleProduct);
        }
        return new SaleDTO(saleRepository.create(sale));
    }

    /**
     * This method lists all sales saved in the database.
     *
     * @return List of all sales
     */
    public List<SaleDTO> findAll() {
        List<Sale> sales = saleRepository.findAll();
        List<SaleDTO> saleDTOS = new ArrayList<>();
        for (Sale sale : sales) {
            saleDTOS.add(new SaleDTO(sale));
        }
        return saleDTOS;
    }

    /**
     * Fetches and turns the sale with the given id into a sale data transfer object and returns it.
     *
     * @param id the id of the sale to be shown
     * @return the sale with the given id
     */
    public SaleDTO findById(int id) {
        Sale sale = saleRepository.findById(id);
        if (sale == null) return null;
        return new SaleDTO(sale);
    }

    /**
     * Deletes the sale with the given id from the database.
     *
     * @param id the id of the sale to be deleted
     */
    public void deletePermanentById(int id) {
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

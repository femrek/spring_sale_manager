package dev.faruk.product.service;

import dev.faruk.commoncodebase.dto.ProductDTO;
import dev.faruk.commoncodebase.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.faruk.product.dao.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * @return all products from database.
     */
    public List<ProductDTO> getAllProducts() {
        List<Product> data =  productRepository.findAll();
        return data.stream().map(ProductDTO::new).toList();
    }

    /**
     * @param id product id
     * @return the product with given id from database.
     */
    public ProductDTO getProductById(int id) {
        Product product = productRepository.findById(id);
        return new ProductDTO(product);
    }
}

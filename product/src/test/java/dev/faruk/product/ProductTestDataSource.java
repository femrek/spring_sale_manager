package dev.faruk.product;

import dev.faruk.commoncodebase.entity.Product;
import dev.faruk.commoncodebase.repository.base.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductTestDataSource {
    private final ProductRepository productRepository;

    public ProductTestDataSource(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void cleanDatabase() {
        List<Long> ids = productRepository.findAll().stream().map(Product::getId).toList();
        for (Long id : ids) {
            productRepository.deleteById(id);
        }
    }

    public void loadDatabase() {
        List<Product> products = _generateProducts();
        for (Product product : products) {
            productRepository.create(product);
        }
    }

    private List<Product> _generateProducts() {
        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .name("Product 1")
                .price(100.0)
                .barcode("32134560101")
                .build());
        products.add(Product.builder()
                .name("Product 2")
                .price(20.0)
                .barcode("32134560102")
                .build());
        products.add(Product.builder()
                .name("Product 3")
                .price(30.0)
                .barcode("32134560103")
                .build());
        products.add(Product.builder()
                .name("Product 4")
                .price(45.5)
                .barcode("32134560104")
                .build());
        products.add(Product.builder()
                .name("Product 5")
                .price(14.9)
                .barcode("32134560105")
                .build());
        return products;
    }
}

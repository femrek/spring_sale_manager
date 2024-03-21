package dev.faruk.product;

import dev.faruk.product.controller.ProductController;
import dev.faruk.commoncodebase.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProductApplicationTests {
    private final ProductController productController;

    @Autowired
    ProductApplicationTests(ProductController productController) {
        this.productController = productController;
    }

    @Test
    void fetchAllAndFetchById() {
        // fetch all products
        final List<ProductDTO> products =  productController.showAll().getData();
        assert !products.isEmpty();

        // fetch the first product by id
        final Long firstProductId = products.get(0).getId();
        final ProductDTO theProduct = productController.show(firstProductId).getData();
        assert products.get(0).getBarcode().equals(theProduct.getBarcode());
    }
}

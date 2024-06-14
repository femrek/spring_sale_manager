package dev.faruk.product.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.ProductDTO;
import dev.faruk.product.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController()
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/", ""})
    public AppSuccessResponse<List<ProductDTO>> showAll() {
        final List<ProductDTO> products = productService.getAllProducts();
        return new AppSuccessResponse<>("All products are listed", products);
    }

    @GetMapping("/{id}")
    public AppSuccessResponse<ProductDTO> show(@PathVariable Long id) {
        final ProductDTO product = productService.getProductById(id);
        return new AppSuccessResponse<>("Product is found", product);
    }
}

package com.yordanov.warehouse.Product.Service;

import com.yordanov.warehouse.Exception.BadRequestException;
import com.yordanov.warehouse.Exception.ConflictException;
import com.yordanov.warehouse.Exception.ResourceNotFoundException;
import com.yordanov.warehouse.Product.Model.Product;
import com.yordanov.warehouse.Product.Repository.ProductRepository;
import com.yordanov.warehouse.Web.Dto.ProductRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(ProductRequest productRequest) {

        Optional<Product> optionalProduct = productRepository.findProductBySku(productRequest.getSku());

        if (optionalProduct.isPresent()) {
            throw new ConflictException("Product with SKU: %s already exists".formatted(productRequest.getSku()));
        }

        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .sku(productRequest.getSku())
                .createdAt(LocalDateTime.now())
                .build();
        productRepository.save(product);

         return product;
    }


    public List<Product> findAllProducts() {

        return productRepository.findAll();
    }

    public Product getProductById(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new ResourceNotFoundException("Product with id: %s not found".formatted(id)));
    }

    public Product updateProductById(UUID id, ProductRequest productRequest) {

        Product product = getProductById(id);

        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setSku(productRequest.getSku());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
        return product;
    }

    public void deleteProductById(UUID id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id: %s not found".formatted(id)));

        productRepository.delete(product);

    }
}

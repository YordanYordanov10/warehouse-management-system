package com.yordanov.warehouse.Web;

import com.yordanov.warehouse.Product.Model.Product;
import com.yordanov.warehouse.Product.Service.ProductService;
import com.yordanov.warehouse.Web.Dto.ProductRequest;
import com.yordanov.warehouse.Web.Dto.ProductResponse;
import com.yordanov.warehouse.Web.Mapper.DtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Products", description = "Product management")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products",
    description = "Returns all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Products not found")
    })
    @GetMapping()
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        List<Product> products = productService.findAllProducts();
        List<ProductResponse> productResponses = DtoMapper.toProductResponseList(products);
        return new ResponseEntity<>(productResponses, HttpStatus.OK);
    }

    @Operation(summary = "Create a new product",
            description = "Creates a new product with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping()
    ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest){

        Product product = productService.addProduct(productRequest);
        ProductResponse productResponse = DtoMapper.toProductResponse(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);

    }

    @Operation(summary = "Get product by id",
            description = "Returns a product by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<ProductResponse> getProduct(@PathVariable UUID id) {

        Product product = productService.getProductById(id);
        ProductResponse productResponse = DtoMapper.toProductResponse(product);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @Operation(summary = "Update product by id",
            description = "Updates a product by its id with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID id, @RequestBody ProductRequest productRequest) {

        Product product = productService.updateProductById(id,productRequest);
        ProductResponse productResponse = DtoMapper.toProductResponse(product);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @Operation(summary = "Delete product by id",
            description = "Deletes a product by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<ProductResponse> deleteProduct(@PathVariable UUID id) {

       productService.deleteProductById(id);
       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Product;
import com.example.demo.servises.ProductService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/all")
    public List<Product> getAllProductsController() {
        return  productService.getAllProducts();
    }


    @PostMapping("/insert")
    public Product insertProductInDb(@RequestBody Product product) {
        return  productService.insertProductIntoDatabase(product);
    }

    @GetMapping("/find/{id}")
    public Product getProductById(@PathVariable int id) {
        return  productService.getProductById(id);
    }

    @GetMapping
    private APIResponse<List<Product>> getProducts() {
        List<Product> allProducts = productService.getAllProducts();
        return new APIResponse<>(allProducts.size(), allProducts);
    }

    @GetMapping("/{field}")
    private APIResponse<List<Product>> getProductsWithSort(@PathVariable String field) {
        List<Product> allProducts = productService.findProductsWithSorting(field);
        return new APIResponse<>(allProducts.size(), allProducts);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    private APIResponse<Page<Product>> getProductsWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Product> productsWithPagination = productService.findProductsWithPagination(offset, pageSize);
        return new APIResponse<>(productsWithPagination.getSize(), productsWithPagination);
    }

    @GetMapping("/paginationAndSort/{offset}/{pageSize}/{field}")
    private APIResponse<Page<Product>> getProductsWithPaginationAndSort(@PathVariable int offset, @PathVariable int pageSize,@PathVariable String field) {
        Page<Product> productsWithPagination = productService.findProductsWithPaginationAndSorting(offset, pageSize, field);
        return new APIResponse<>(productsWithPagination.getSize(), productsWithPagination);
    }



    @GetMapping("/{field1}")
    private List<Product> getProductsWithSort2(@PathVariable String field) {
        List<Product> allProducts = productService.findProductsWithSorting(field);
        return allProducts;
    }

    @GetMapping("/pagination1/{offset}/{pageSize}")
    private Page<Product> getProductsWithPagination2(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Product> productsWithPagination = productService.findProductsWithPagination(offset, pageSize);
        return productsWithPagination;
    }

    @GetMapping("/paginationAndSort1/{offset}/{pageSize}/{field}")
    private Page<Product> getProductsWithPaginationAndSort2(@PathVariable int offset, @PathVariable int pageSize,@PathVariable String field) {
        Page<Product> productsWithPagination = productService.findProductsWithPaginationAndSorting(offset, pageSize, field);
        return productsWithPagination ;
    }

    @GetMapping("/download")
    private ResponseEntity<InputStreamResource> download() throws IOException {
          String fileName ="products.xlsx";
         ByteArrayInputStream inputStream = productService.getDataDownloaded();
         InputStreamResource    response = new InputStreamResource(inputStream);

         ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok()
                 .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+fileName)
                 .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(response);
         return responseEntity;
    }

}
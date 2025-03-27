package org.example.liquorice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.liquorice.dtos.PagedResponse;
import org.example.liquorice.dtos.ProductPreviewDto;
import org.example.liquorice.services.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.liquorice.config.AppConfig.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH + "/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public PagedResponse<ProductPreviewDto> getProducts(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) String sort) {

        if (sort != null && !sort.isEmpty()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(sort.split(","))
            );
        }

        return productService.getProductPreviewDtos(pageable, search, categories);
    }


    @GetMapping("/categories")
    public List<String> getCategories() {
        return productService.getAllCategories();
    }

    @PatchMapping("/{id}/available")
    public ResponseEntity<ProductPreviewDto> setAvailable(@RequestBody boolean isAvailable, @PathVariable String id) {
        return ResponseEntity.ok(productService.setAvailable(id, isAvailable));
    }
}
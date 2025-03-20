package org.example.liquorice.services;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.example.liquorice.dtos.PagedResponse;
import org.example.liquorice.dtos.ProductPreviewDto;
import org.example.liquorice.models.Product;
import org.example.liquorice.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final MongoTemplate mongoTemplate;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public PagedResponse<ProductPreviewDto> getProductPreviewDtos(Pageable pageable, String search, List<String> categoryNames) {
        Page<Product> productPage;

        if (search != null && !search.isEmpty() && categoryNames != null && !categoryNames.isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCaseAndCategoriesIn(search, categoryNames, pageable);
        } else if (search != null && !search.isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if (categoryNames != null && !categoryNames.isEmpty()) {
            productPage = productRepository.findByCategoriesIn(categoryNames, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<ProductPreviewDto> productDtos = productPage.getContent().stream()
                        .map(this::mapToProductPreviewDto)
                        .collect(Collectors.toList());

        return new PagedResponse<>(
                productDtos,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );
    }

    public List<String> getAllCategories() {
        return mongoTemplate.getCollection("products")
                .distinct("categories", String.class)
                .into(new ArrayList<>());
    }

    public ProductPreviewDto mapToProductPreviewDto(Product product) {
        ProductPreviewDto dto = modelMapper.map(product, ProductPreviewDto.class);
        if (product.getImage() != null) {
            dto.setImage(Base64.getEncoder().encodeToString(product.getImage()));
        }
        return dto;
    }
}
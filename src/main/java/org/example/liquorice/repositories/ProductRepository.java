package org.example.liquorice.repositories;

import org.example.liquorice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;

public interface ProductRepository extends MongoRepository<Product, String> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByCategoriesIn(Collection<String> categories, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseAndCategoriesIn(String name, Collection<String> categoriesNames, Pageable pageable);
}
package org.example.liquorice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private int id;
    private String name;
    private String description;
    private double price;
    private byte[] image;
    @DBRef
    private List<Category> categories;
    private int amountLeft;
}

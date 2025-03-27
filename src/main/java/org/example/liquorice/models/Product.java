package org.example.liquorice.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private byte[] image;
    private List<String> categories;
    private int amountLeft;
    private boolean isAvailable;
}

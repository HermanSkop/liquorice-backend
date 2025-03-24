package org.example.liquorice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPreviewDto {
    private String id;
    private String name;
    private String description;
    private double price;
    private List<String> categories;
    private String image;
    private int amountLeft;
}
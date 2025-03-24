package org.example.liquorice.dtos;

import lombok.Data;

@Data
public class AddressDto {
    private String city;
    private String country = "US";
    private String line1;
    private String line2;
    private String postalCode;
    private String state;
}

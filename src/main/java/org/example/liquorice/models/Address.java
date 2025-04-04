package org.example.liquorice.models;

import lombok.Data;

@Data
public class Address {
    private String city;
    private String country = "US";
    private String line1;
    private String line2;
    private String postalCode;
    private String state;
}

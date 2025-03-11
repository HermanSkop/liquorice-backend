package org.example.liquorice.model.user;

import org.example.liquorice.model.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public abstract class User {
    @Id
    int id;
    String firstName;
    String lastName;
    String email;
    String password;
    Address address;
}

package org.example.liquorice.models.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.liquorice.config.AppConfig;
import org.example.liquorice.models.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public abstract class User implements UserDetails {
    @Id
    String id;
    String firstName;
    String lastName;
    @Email
    String email;
    @Pattern(regexp = AppConfig.PASSWORD_REGEX, message = AppConfig.PASSWORD_REGEX_MESSAGE)
    String password;
    Address address;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = this.getClass().getSimpleName().toUpperCase();
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

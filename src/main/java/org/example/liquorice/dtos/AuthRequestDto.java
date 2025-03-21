package org.example.liquorice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.liquorice.config.AppConfig;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {
    @Email
    private String email;
    @Pattern(regexp = AppConfig.PASSWORD_REGEX, message = AppConfig.PASSWORD_REGEX_MESSAGE)
    private String password;
}

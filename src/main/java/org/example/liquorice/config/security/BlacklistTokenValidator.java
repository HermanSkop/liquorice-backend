package org.example.liquorice.config.security;

import lombok.RequiredArgsConstructor;
import org.example.liquorice.services.TokenBlacklistService;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlacklistTokenValidator implements OAuth2TokenValidator<Jwt> {

    private final TokenBlacklistService tokenBlacklistService;
    private static final OAuth2Error BLACKLISTED_ERROR = new OAuth2Error(
            "invalid_token",
            "The token has been blacklisted",
            null
    );


    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (tokenBlacklistService.isTokenBlacklisted(jwt.getTokenValue())) {
            return OAuth2TokenValidatorResult.failure(BLACKLISTED_ERROR);
        }
        return OAuth2TokenValidatorResult.success();
    }
}
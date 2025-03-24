package org.example.liquorice.config;

import com.stripe.Stripe;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class AppConfig {
    public final static int JWT_ACCESS_TOKEN_SECONDS_TIMEOUT_SKEW = 10;
    public static final SignatureAlgorithm JWT_SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    public final static String BASE_PATH = "/api/v1";

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    public static final String PASSWORD_REGEX_MESSAGE = "Password must contain at least one letter, one number, one special character, and be at least 8 characters long.";

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }
}

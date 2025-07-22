package learn.petnote.controllers;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class SecretSigningKey {

    private Key signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public Key getSigningKey() {
        return signingKey;
    }

}

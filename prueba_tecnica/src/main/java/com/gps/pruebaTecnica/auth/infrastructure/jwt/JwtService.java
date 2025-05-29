package com.gps.pruebaTecnica.auth.infrastructure.jwt;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
public class JwtService {
    private static final String SECRET_KEY = "gHBmmHYE3C8LjKL5hvkcdTHoI4MvIPn+ZBhhcfwAfaE=";

    public String generateToken(String username, Collection<? extends GrantedAuthority> roles) {
        try {
            JWSSigner signer = new MACSigner(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            List<String> roleList = roles.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .claim("roles", roleList)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet);

            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException("Token inválido", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            if (!signedJWT.verify(verifier)) {
                return false;
            }
            String username = signedJWT.getJWTClaimsSet().getSubject();
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(signedJWT));
        } catch (Exception e) {
            throw new RuntimeException("Token inválido", e);
        }
    }

    private boolean isTokenExpired(SignedJWT signedJWT) {
        try {
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expirationTime != null && expirationTime.before(new Date());
        } catch (ParseException e) {
            throw new RuntimeException("Error checking token expiration", e);
        }
    }
}

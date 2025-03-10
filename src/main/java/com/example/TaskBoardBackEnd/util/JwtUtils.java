package com.example.TaskBoardBackEnd.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.TaskBoardBackEnd.model.User;
import com.example.TaskBoardBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private final UserRepository userRepository;
    @Value("${security.jwt.key.private}")
    private String privateKey;
    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    @Autowired
    public JwtUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createToken(Authentication authentication) {
        Optional<User> user = userRepository.findUserByUserName(authentication.getPrincipal().toString());

        Algorithm algorithm = Algorithm
                .HMAC256(this.privateKey);

        String username = authentication.getName();

        String authorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        if (user.isPresent()) {
            return JWT
                    .create()
                    .withIssuer(userGenerator)
                    .withSubject(username)
                    .withClaim("Authorities", authorities)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
                    .withJWTId(user.get().getId().toString())
                    .withNotBefore(new Date(System.currentTimeMillis()))
                    .sign(algorithm);
        } else {
            throw new IllegalStateException("User not found");
        }
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            JWTVerifier verifier = JWT
                    .require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token invalid: " + exception.getMessage());
        }
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> getClaim(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }

}
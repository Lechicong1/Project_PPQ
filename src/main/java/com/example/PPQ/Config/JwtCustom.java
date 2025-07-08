package com.example.PPQ.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtCustom {

    @Value("${security.authentication.jwt.token-validity-in-seconds}")
    private long tokenValidityInSeconds;
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Autowired
    JwtEncoder jwtEncoder;


    public String createToken(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication cannot be null");
        }

        Instant now = Instant.now();
        Instant validity=now.plus(tokenValidityInSeconds, ChronoUnit.SECONDS);
        // Lấy danh sách quyền (authorities)
        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtClaimsSet claims=JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("authorities",authorities)
                .build();
        JwsHeader jwsHeader=JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,claims)).getTokenValue();  // tao chu ky so gom khoa bi mat va thong diep(payload)
        }
        // token co 3 phan header.payload.signature
        // header : chua thong tin thuat toan ma hoa
        // payload : chua thong tin nguoi dung ( la phan claims)
        //signature : la phan header+payload ma hoa
}

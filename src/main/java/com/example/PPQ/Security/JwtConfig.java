package com.example.PPQ.Security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {
    @Value("${security.authentication.jwt.base64-secret}")
    private String secretKey;

    //giai ma secretkey
    private SecretKey getSecretKey() {
        byte[] keyBytes =Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(keyBytes,0,keyBytes.length,JwtCustom.JWT_ALGORITHM.getName());
    }
    @Bean
    public JwtEncoder JwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()) );
    }
    // giai ma token duoc gui tu header
    @Bean
    public JwtDecoder JwtDecoder() {    // xac thuc chu ky so
        NimbusJwtDecoder  jwtDecoder=NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(JwtCustom.JWT_ALGORITHM).build();
        return token -> {
            try{
                return jwtDecoder.decode(token);
            }
            catch(Exception e){
                return null;
            }
        };
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("authorities"); // claim bạn thêm vào token
        authoritiesConverter.setAuthorityPrefix(""); // nếu bạn đã gắn "ROLE_" trong token rồi

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

}

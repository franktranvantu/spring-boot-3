package com.franktranvantu.springboot3.configuration;

import com.franktranvantu.springboot3.dto.request.IntrospectRequest;
import com.franktranvantu.springboot3.exception.ServiceException;
import com.franktranvantu.springboot3.service.AuthenticationService;
import com.nimbusds.jose.JWSAlgorithm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

import static com.franktranvantu.springboot3.exception.ServiceStatusCode.UNAUTHENTICATED;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    @NonFinal
    String signerKey;

    AuthenticationService authenticationService;
    @Override
    public Jwt decode(String token) throws JwtException {
        IntrospectRequest introspectRequest = IntrospectRequest.builder().token(token).build();
        final var isValid = authenticationService.introspect(introspectRequest).isValid();
        if (!isValid) {
            throw new ServiceException(UNAUTHENTICATED);
        }
        final var secretKey = new SecretKeySpec(signerKey.getBytes(), JWSAlgorithm.HS512.getName());
        final var nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        return nimbusJwtDecoder.decode(token);
    }
}

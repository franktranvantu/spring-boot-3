package com.franktranvantu.springboot3.service;

import com.franktranvantu.springboot3.dto.request.AuthenticationRequest;
import com.franktranvantu.springboot3.dto.request.IntrospectRequest;
import com.franktranvantu.springboot3.dto.request.LogoutRequest;
import com.franktranvantu.springboot3.dto.request.RefreshTokenRequest;
import com.franktranvantu.springboot3.dto.response.AuthenticationResponse;
import com.franktranvantu.springboot3.dto.response.IntrospectResponse;
import com.franktranvantu.springboot3.entity.InvalidatedToken;
import com.franktranvantu.springboot3.entity.Permission;
import com.franktranvantu.springboot3.entity.User;
import com.franktranvantu.springboot3.exception.ServiceException;
import com.franktranvantu.springboot3.repository.InvalidatedTokenRepository;
import com.franktranvantu.springboot3.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import static com.franktranvantu.springboot3.exception.ServiceStatusCode.INVALID_TOKEN;
import static com.franktranvantu.springboot3.exception.ServiceStatusCode.UNAUTHENTICATED;
import static com.franktranvantu.springboot3.exception.ServiceStatusCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    @Value("${jwt.signerKey}")
    @NonFinal
    String signerKey;
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    public AuthenticationResponse authenticated(AuthenticationRequest request) {
        final var passwordEncoder = new BCryptPasswordEncoder(10);
        final var user = userRepository.findUserByUsername(request.getUsername()).orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        final var authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new ServiceException(UNAUTHENTICATED);
        }
        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(generateJwtToken(user))
                .build();
    }

    public String generateJwtToken(User user) {
        final var header = new JWSHeader(JWSAlgorithm.HS512);
        final var roles = user.getRoles().stream().map(role -> "ROLE_" + role.getName());
        final var permissions = user.getRoles().stream().flatMap(role -> role.getPermissions().stream()).map(Permission::getName);
        final var claimsSet = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .claim("scope", String.join(" ", Stream.concat(roles, permissions).toList()))
                .issuer("franktranvantu.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .build();
        final var payload = new Payload(claimsSet.toJSONObject());
        final var jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        var isValid = true;
        try {
            verifyToken(request.getToken());
        } catch (ServiceException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private SignedJWT verifyToken(String token) {
        try {
            final MACVerifier verifier = new MACVerifier(signerKey.getBytes());
            final var signedJWT = SignedJWT.parse(token);
            final var validSigned = signedJWT.verify(verifier);
            final var validExpirationTime = signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date());
            final var valid = validSigned && validExpirationTime;
            if (!valid) {
                throw new ServiceException(INVALID_TOKEN);
            }
            final var isLoggedOutToken = invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID());
            if (isLoggedOutToken) {
                throw new ServiceException(UNAUTHENTICATED);
            }
            return signedJWT;
        } catch (JOSEException | ParseException e) {
            log.error("Cannot sign or parse token", e);
            throw new RuntimeException(e);
        }
    }

    public void logout(LogoutRequest request) {
        final var signedJWT = verifyToken(request.getToken());
        invalidateToken(signedJWT);
    }

    public AuthenticationResponse refresh(RefreshTokenRequest request) {
        try {
            final var signedJWT = verifyToken(request.getToken());
            invalidateToken(signedJWT);
            final var username = signedJWT.getJWTClaimsSet().getSubject();
            final var user = userRepository.findUserByUsername(username).orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
            return AuthenticationResponse.builder()
                    .authenticated(true)
                    .token(generateJwtToken(user))
                    .build();
        } catch (ParseException e) {
            log.error("Cannot parse token", e);
            throw new RuntimeException(e);
        }
    }

    private void invalidateToken(SignedJWT signedJWT) {
        try {
            final var jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            final var expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            final var invalidatedToken = InvalidatedToken.builder()
                    .id(jwtId)
                    .expiryTime(expirationTime)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (ParseException e) {
            log.error("Cannot parse token", e);
            throw new RuntimeException(e);
        }
    }
}

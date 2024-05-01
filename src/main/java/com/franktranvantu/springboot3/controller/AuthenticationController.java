package com.franktranvantu.springboot3.controller;

import com.franktranvantu.springboot3.dto.request.AuthenticationRequest;
import com.franktranvantu.springboot3.dto.request.IntrospectRequest;
import com.franktranvantu.springboot3.dto.request.LogoutRequest;
import com.franktranvantu.springboot3.dto.request.RefreshTokenRequest;
import com.franktranvantu.springboot3.dto.response.AuthenticationResponse;
import com.franktranvantu.springboot3.dto.response.IntrospectResponse;
import com.franktranvantu.springboot3.dto.response.ServiceResponse;
import com.franktranvantu.springboot3.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ServiceResponse<AuthenticationResponse> authenticated(@RequestBody AuthenticationRequest request) {
        return ServiceResponse.ok(authenticationService.authenticated(request));
    }

    @PostMapping("/refresh")
    public ServiceResponse<AuthenticationResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ServiceResponse.ok(authenticationService.refresh(request));
    }

    @PostMapping("/introspect")
    public ServiceResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        return ServiceResponse.ok(authenticationService.introspect(request));
    }

    @PostMapping("/logout")
    public ServiceResponse<Void> logout(@RequestBody LogoutRequest request) {
        authenticationService.logout(request);
        return ServiceResponse.ok();
    }
}

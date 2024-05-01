package com.franktranvantu.springboot3.controller;

import com.franktranvantu.springboot3.dto.request.UserCreationRequest;
import com.franktranvantu.springboot3.dto.request.UserUpdateRequest;
import com.franktranvantu.springboot3.dto.response.ServiceResponse;
import com.franktranvantu.springboot3.dto.response.UserResponse;
import com.franktranvantu.springboot3.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ServiceResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ServiceResponse.ok(userService.createUser(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ServiceResponse<List<UserResponse>> getUsers() {
        return ServiceResponse.ok(userService.getUsers());
    }

    @GetMapping("/{userId}")
    @PostAuthorize("returnObject.result.username == authentication.name || hasRole('ADMIN')")
    public ServiceResponse<UserResponse> getUser(@PathVariable String userId) {
        return ServiceResponse.ok(userService.getUser(userId));
    }

    @PutMapping("/{userId}")
    @PostAuthorize("returnObject.result.username == authentication.name || hasRole('ADMIN')")
    public ServiceResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ServiceResponse.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ServiceResponse deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ServiceResponse.ok();
    }

    @GetMapping("/my-info")
    @PostAuthorize("returnObject.username == authentication.name")
    public ServiceResponse<UserResponse> getMyInfo() {
        return ServiceResponse.ok(userService.getMyInfo());
    }
}

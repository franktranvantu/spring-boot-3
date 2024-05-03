package com.franktranvantu.springboot3.service;

import static com.franktranvantu.springboot3.enums.Role.USER;
import static com.franktranvantu.springboot3.exception.ServiceStatusCode.USER_EXISTED;
import static com.franktranvantu.springboot3.exception.ServiceStatusCode.USER_NOT_FOUND;

import com.franktranvantu.springboot3.dto.request.UserCreationRequest;
import com.franktranvantu.springboot3.dto.request.UserUpdateRequest;
import com.franktranvantu.springboot3.dto.response.UserResponse;
import com.franktranvantu.springboot3.exception.ServiceException;
import com.franktranvantu.springboot3.mapper.UserMapper;
import com.franktranvantu.springboot3.repository.RoleRepository;
import com.franktranvantu.springboot3.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ServiceException(USER_EXISTED);
        }
        final var userRoles = Set.of(roleRepository.getReferenceById(USER.name()));
        final var user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(userRoles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser(String userId) {
        return userRepository
                .findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        final var user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        userMapper.userUpdateRequestToUser(request, user);
        final var roles = roleRepository.findAllById(request.getRoles());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public UserResponse getMyInfo() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        final var username = authentication.getName();
        return userMapper.toUserResponse(
                userRepository.findUserByUsername(username).orElseThrow(() -> new ServiceException(USER_NOT_FOUND)));
    }
}

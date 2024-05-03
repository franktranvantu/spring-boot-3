package com.franktranvantu.springboot3.service;

import static com.franktranvantu.springboot3.exception.ServiceStatusCode.PERMISSION_EXISTED;
import static com.franktranvantu.springboot3.exception.ServiceStatusCode.PERMISSION_NOT_FOUND;

import com.franktranvantu.springboot3.dto.request.PermissionRequest;
import com.franktranvantu.springboot3.dto.response.PermissionResponse;
import com.franktranvantu.springboot3.exception.ServiceException;
import com.franktranvantu.springboot3.mapper.PermissionMapper;
import com.franktranvantu.springboot3.repository.PermissionRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest request) {
        if (permissionRepository.findById(request.getName()).isPresent()) {
            throw new ServiceException(PERMISSION_EXISTED);
        }
        final var permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    public List<PermissionResponse> getPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public PermissionResponse getPermission(String permissionName) {
        return permissionRepository
                .findById(permissionName)
                .map(permissionMapper::toPermissionResponse)
                .orElseThrow(() -> new ServiceException(PERMISSION_NOT_FOUND));
    }

    public PermissionResponse updatePermission(String permissionName, PermissionRequest request) {
        final var permission = permissionRepository
                .findById(permissionName)
                .orElseThrow(() -> new ServiceException(PERMISSION_NOT_FOUND));
        permissionMapper.permissionUpdateRequestToPermission(request, permission);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    public void deletePermission(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }
}

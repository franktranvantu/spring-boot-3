package com.franktranvantu.springboot3.controller;

import com.franktranvantu.springboot3.dto.request.PermissionRequest;
import com.franktranvantu.springboot3.dto.response.PermissionResponse;
import com.franktranvantu.springboot3.dto.response.ServiceResponse;
import com.franktranvantu.springboot3.service.PermissionService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ServiceResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        return ServiceResponse.ok(permissionService.createPermission(request));
    }

    @GetMapping
    public ServiceResponse<List<PermissionResponse>> getPermissions() {
        return ServiceResponse.ok(permissionService.getPermissions());
    }

    @GetMapping("/{permissionName}")
    public ServiceResponse<PermissionResponse> getPermission(@PathVariable String permissionName) {
        return ServiceResponse.ok(permissionService.getPermission(permissionName));
    }

    @PutMapping("/{permissionName}")
    public ServiceResponse<PermissionResponse> updatePermission(
            @PathVariable String permissionName, @RequestBody PermissionRequest request) {
        return ServiceResponse.ok(permissionService.updatePermission(permissionName, request));
    }

    @DeleteMapping("/{permissionName}")
    public ServiceResponse<Void> deletePermission(@PathVariable String permissionName) {
        permissionService.deletePermission(permissionName);
        return ServiceResponse.ok();
    }
}

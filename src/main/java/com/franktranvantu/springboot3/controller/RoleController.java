package com.franktranvantu.springboot3.controller;

import com.franktranvantu.springboot3.dto.request.RoleRequest;
import com.franktranvantu.springboot3.dto.response.RoleResponse;
import com.franktranvantu.springboot3.dto.response.ServiceResponse;
import com.franktranvantu.springboot3.service.RoleService;
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

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ServiceResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ServiceResponse.ok(roleService.createRole(request));
    }

    @GetMapping
    public ServiceResponse<List<RoleResponse>> getRoles() {
        return ServiceResponse.ok(roleService.getRoles());
    }

    @GetMapping("/{roleName}")
    public ServiceResponse<RoleResponse> getRole(@PathVariable String roleName) {
        return ServiceResponse.ok(roleService.getRole(roleName));
    }

    @PutMapping("/{roleName}")
    public ServiceResponse<RoleResponse> updateRole(@PathVariable String roleName, @RequestBody RoleRequest request) {
        return ServiceResponse.ok(roleService.updateRole(roleName, request));
    }

    @DeleteMapping("/{roleName}")
    public ServiceResponse<Void> deleteRole(@PathVariable String roleName) {
        roleService.deleteRole(roleName);
        return ServiceResponse.ok();
    }
}

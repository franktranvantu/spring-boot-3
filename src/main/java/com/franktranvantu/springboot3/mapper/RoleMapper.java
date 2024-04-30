package com.franktranvantu.springboot3.mapper;

import com.franktranvantu.springboot3.dto.request.RoleRequest;
import com.franktranvantu.springboot3.dto.response.PermissionResponse;
import com.franktranvantu.springboot3.dto.response.RoleResponse;
import com.franktranvantu.springboot3.entity.Permission;
import com.franktranvantu.springboot3.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest dto);
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleResponse dto);
    @Mapping(source = "permissions", target = "permissions", qualifiedByName = "mapPermissions")
    RoleResponse toRoleResponse(Role entity);
    @Named("mapPermissions")
    default Set<PermissionResponse> mapPermissions(Set<Permission> permissions) {
        return permissions
                .stream()
                .map(permission -> PermissionResponse
                        .builder()
                        .name(permission.getName())
                        .description(permission.getDescription())
                        .build()
                )
                .collect(Collectors.toSet());
    }
    @Mapping(target = "permissions", ignore = true)
    void roleUpdateRequestToRole(RoleRequest dto, @MappingTarget Role entity);
}

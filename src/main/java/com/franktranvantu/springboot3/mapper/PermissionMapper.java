package com.franktranvantu.springboot3.mapper;

import com.franktranvantu.springboot3.dto.request.PermissionRequest;
import com.franktranvantu.springboot3.dto.response.PermissionResponse;
import com.franktranvantu.springboot3.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {
    Permission toPermission(PermissionRequest dto);

    Permission toPermission(PermissionResponse dto);

    PermissionResponse toPermissionResponse(Permission entity);

    void permissionUpdateRequestToPermission(PermissionRequest dto, @MappingTarget Permission entity);
}

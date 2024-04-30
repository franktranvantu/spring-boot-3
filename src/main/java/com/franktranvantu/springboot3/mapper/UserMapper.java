package com.franktranvantu.springboot3.mapper;

import com.franktranvantu.springboot3.dto.request.UserCreationRequest;
import com.franktranvantu.springboot3.dto.request.UserUpdateRequest;
import com.franktranvantu.springboot3.dto.response.RoleResponse;
import com.franktranvantu.springboot3.dto.response.UserResponse;
import com.franktranvantu.springboot3.entity.Role;
import com.franktranvantu.springboot3.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "dob", target = "dateOfBirth")
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest dto);
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "dob", target = "dateOfBirth")
    @Mapping(target = "roles", ignore = true)
    User toUser(UserUpdateRequest dto);
    @Mapping(source = "dateOfBirth", target = "dob")
    @Mapping(target = "roles", qualifiedByName = "mapRoles")
    UserResponse toUserResponse(User entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "dob", target = "dateOfBirth")
    @Mapping(target = "roles", ignore = true)
    void userUpdateRequestToUser(UserUpdateRequest dto, @MappingTarget User entity);
    @Named("mapRoles")
    default Set<RoleResponse> mapRoles(Set<Role> roles) {
        RoleMapper roleMapper = new RoleMapperImpl();
        return roles.stream()
                .map(role -> RoleResponse
                        .builder()
                        .name(role.getName())
                        .description(role.getDescription())
                        .permissions(roleMapper.mapPermissions(role.getPermissions()))
                        .build()
                )
                .collect(Collectors.toSet());
    }
}

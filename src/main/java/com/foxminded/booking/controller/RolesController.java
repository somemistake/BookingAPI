package com.foxminded.booking.controller;

import com.foxminded.booking.model.Role;
import com.foxminded.booking.model.dto.RoleDto;
import com.foxminded.booking.model.exception.NotFoundException;
import com.foxminded.booking.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.foxminded.booking.security.SecurityConstants.isAdmin;

@RestController
@PreAuthorize(isAdmin)
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles Controller", description = "Working with roles")
public class RolesController {
    private final RoleService roleService;

    public RolesController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @Operation(summary = "Get all roles", security = @SecurityRequirement(name = "bearerAuth"))
    public List<RoleDto> getRoles() {
        return roleService
                .findAll()
                .stream()
                .map(Role::toRoleDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by id", security = @SecurityRequirement(name = "bearerAuth"))
    public RoleDto getRoleById(@Parameter(description = "role id", required = true)
                               @PathVariable("id") long id) {
        return roleService
                .findById(id)
                .orElseThrow(() -> new NotFoundException("role not found", HttpStatus.NOT_FOUND))
                .toRoleDto();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create role", security = @SecurityRequirement(name = "bearerAuth"))
    public RoleDto createRole(@RequestBody(
            description = "Role to add",
            required = true,
            content = @Content(schema = @Schema(implementation = Role.class))
    ) @org.springframework.web.bind.annotation.RequestBody @Valid Role role) {
        return roleService.save(role).toRoleDto();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit role by id", security = @SecurityRequirement(name = "bearerAuth"))
    public RoleDto editRole(@Parameter(description = "role id", required = true)
                            @PathVariable("id") long id,
                            @RequestBody(
                                    description = "Role to edit",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = Role.class))
                            ) @org.springframework.web.bind.annotation.RequestBody @Valid Role actual) {
        return roleService.findById(id)
                .map(role -> {
                    role.setName(actual.getName());
                    role.setUsers(actual.getUsers());
                    return roleService.save(role);
                })
                .orElseThrow(() -> new NotFoundException("role not found", HttpStatus.NOT_FOUND))
                .toRoleDto();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete role by id", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteRole(@Parameter(description = "role id", required = true)
                           @PathVariable("id") long id) {
        roleService.deleteById(id);
    }
}

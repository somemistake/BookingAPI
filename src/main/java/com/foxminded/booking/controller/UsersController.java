package com.foxminded.booking.controller;

import com.foxminded.booking.model.User;
import com.foxminded.booking.model.dto.UserDto;
import com.foxminded.booking.model.exception.NotFoundException;
import com.foxminded.booking.service.UserService;
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
import static com.foxminded.booking.security.SecurityConstants.isAuthenticated;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users Controller", description = "Working with users")
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize(isAdmin)
    @GetMapping
    @Operation(summary = "Get all users", security = @SecurityRequirement(name = "bearerAuth"))
    public List<UserDto> getUsers() {
        return userService
                .findAll()
                .stream()
                .map(User::toUserDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize(isAuthenticated)
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", security = @SecurityRequirement(name = "bearerAuth"))
    public UserDto getUserById(@Parameter(description = "user id", required = true)
                               @PathVariable("id") long id) {
        return userService
                .findById(id)
                .orElseThrow(() -> new NotFoundException("user not found", HttpStatus.NOT_FOUND))
                .toUserDto();
    }

    @PreAuthorize(isAdmin)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user", security = @SecurityRequirement(name = "bearerAuth"))
    public UserDto createUser(@RequestBody(
            description = "User to add",
            required = true,
            content = @Content(schema = @Schema(implementation = User.class))
    ) @org.springframework.web.bind.annotation.RequestBody @Valid User user) {
        return userService.save(user).toUserDto();
    }

    @PreAuthorize(isAuthenticated)
    @PutMapping("/{id}")
    @Operation(summary = "Edit user by id", security = @SecurityRequirement(name = "bearerAuth"))
    public UserDto editUser(@Parameter(description = "user id", required = true)
                            @PathVariable("id") long id,
                            @RequestBody(
                                    description = "user to edit",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = User.class))
                            ) @org.springframework.web.bind.annotation.RequestBody @Valid User actual) {
        return userService.findById(id)
                .map(user -> {
                    user.setFirstName(actual.getFirstName());
                    user.setLastName(actual.getLastName());
                    user.setUsername(actual.getUsername());
                    user.setPassword(actual.getPassword());
                    user.setRole(actual.getRole());
                    user.setBookings(actual.getBookings());
                    return userService.save(user);
                })
                .orElseThrow(() -> new NotFoundException("user not found", HttpStatus.NOT_FOUND))
                .toUserDto();
    }

    @PreAuthorize(isAdmin)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteUser(@Parameter(description = "user id", required = true)
                           @PathVariable("id") long id) {
        userService.deleteById(id);
    }
}

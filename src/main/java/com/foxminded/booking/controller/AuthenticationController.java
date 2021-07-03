package com.foxminded.booking.controller;

import com.foxminded.booking.model.User;
import com.foxminded.booking.model.dto.AuthorizationDto;
import com.foxminded.booking.model.dto.RegistrationDto;
import com.foxminded.booking.model.dto.UserDto;
import com.foxminded.booking.model.exception.NotFoundException;
import com.foxminded.booking.security.jwt.JwtTokenProvider;
import com.foxminded.booking.service.RoleService;
import com.foxminded.booking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Tag(name = "Authentication Controller", description = "Working with user authentication")
public class AuthenticationController {
    private final UserService userService;
    private final RoleService roleService;
    private final JwtTokenProvider tokenProvider;

    public AuthenticationController(UserService userService, RoleService roleService, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.roleService = roleService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    @Operation(summary = "Register user")
    public ResponseEntity<UserDto> registerUser(@RequestBody(
            description = "Registration to add user",
            required = true,
            content = @Content(schema = @Schema(implementation = RegistrationDto.class))
    ) @org.springframework.web.bind.annotation.RequestBody @Valid RegistrationDto dto) {
        User user = new User(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getUsername(),
                dto.getPassword(),
                roleService.findByName("ROLE_USER")
                        .orElseThrow(() -> new NotFoundException("role not found", HttpStatus.NO_CONTENT))
        );
        return new ResponseEntity<>(userService.save(user).toUserDto(), HttpStatus.OK);
    }

    @PostMapping("/auth")
    @Operation(summary = "Auth user")
    public ResponseEntity<String> authUser(@RequestBody(
            description = "User authorization",
            required = true,
            content = @Content(schema = @Schema(implementation = AuthorizationDto.class))
    ) @org.springframework.web.bind.annotation.RequestBody @Valid AuthorizationDto dto) {
        Optional<User> user = userService.findByUsername(dto.getUsername());
        if (user.isPresent() && user.get().getPassword().equals(dto.getPassword()))
            return new ResponseEntity<>(tokenProvider.generateToken(dto.getUsername()), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

package com.foxminded.booking.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "User Entity")
public class UserDto {
    @Schema(description = "Identifier")
    private long id;
    @Schema(description = "User Last Name")
    private String firstName;
    @Schema(description = "User Last Name")
    private String lastName;
    @Schema(description = "Username")
    private String username;
    @Schema(description = "Role Name")
    private String role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto dto = (UserDto) o;
        return firstName.equals(dto.firstName) &&
                lastName.equals(dto.lastName) &&
                username.equals(dto.username) &&
                role.equals(dto.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, username, role);
    }
}

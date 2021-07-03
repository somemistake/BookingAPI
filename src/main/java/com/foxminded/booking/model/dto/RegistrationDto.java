package com.foxminded.booking.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Schema(description = "Registration Entity")
public class RegistrationDto {
    @NotEmpty
    @Schema(description = "User First Name")
    private String firstName;
    @NotEmpty
    @Schema(description = "User Last Name")
    private String lastName;
    @NotEmpty
    @Schema(description = "Username")
    private String username;
    @NotEmpty
    @Schema(description = "Password")
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationDto that = (RegistrationDto) o;
        return firstName.equals(that.firstName) &&
                lastName.equals(that.lastName) &&
                username.equals(that.username) &&
                password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, username, password);
    }
}

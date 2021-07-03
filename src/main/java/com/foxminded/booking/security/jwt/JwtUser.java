package com.foxminded.booking.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

@Component
public class JwtUser implements UserDetails {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> roles;

    public JwtUser() {
    }

    public JwtUser(String firstName, String lastName, String username, String password, Collection<? extends GrantedAuthority> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(Collection<? extends GrantedAuthority> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtUser jwtUser = (JwtUser) o;
        return firstName.equals(jwtUser.firstName) &&
                lastName.equals(jwtUser.lastName) &&
                username.equals(jwtUser.username) &&
                password.equals(jwtUser.password) &&
                roles.equals(jwtUser.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, username, password, roles);
    }
}

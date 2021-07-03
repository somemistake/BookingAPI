package com.foxminded.booking.service;

import com.foxminded.booking.model.Role;
import com.foxminded.booking.model.User;
import com.foxminded.booking.repository.UserRepository;
import com.foxminded.booking.security.jwt.JwtUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @MockBean
    private UserRepository repository;

    @Autowired
    private UserService service;

    @Test
    void shouldFindById() {
        Optional<User> expected = Optional.of(new User());
        when(repository.findById(1l)).thenReturn(expected);
        assertEquals(expected, service.findById(1l));
    }

    @Test
    void shouldFindByName() {
        Optional<User> expected = Optional.of(new User());
        when(repository.findByUsername("a")).thenReturn(expected);
        assertEquals(expected, service.findByUsername("a"));
    }

    @Test
    void shouldFindAll() {
        List<User> expected = new ArrayList<>();
        when(repository.findAll()).thenReturn(expected);
        assertEquals(expected, service.findAll());
    }

    @Test
    void shouldPersistInstance() {
        User expected = new User();
        when(repository.save(expected)).thenReturn(expected);
        assertEquals(expected, service.save(expected));
    }

    @Test
    void shouldDeleteById() {
        doNothing().when(repository).deleteById(1l);
        service.deleteById(1l);
        verify(repository, times(1)).deleteById(1l);
    }

    @Test
    void shouldLoadUserByUsername() {
        JwtUser expected =
                new JwtUser(
                        "a",
                        "a",
                        "a",
                        "a",
                        new ArrayList<SimpleGrantedAuthority>() {{
                            add(new SimpleGrantedAuthority("ROLE_USER"));
                        }}
                );
        when(repository.findByUsername("a"))
                .thenReturn(Optional.of(new User(
                        "a", "a", "a", "a",
                        new Role(1l, "ROLE_USER")
                )));
        assertEquals(expected, service.loadUserByUsername("a"));
    }
}
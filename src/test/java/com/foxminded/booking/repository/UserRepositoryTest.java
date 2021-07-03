package com.foxminded.booking.repository;

import com.foxminded.booking.model.Role;
import com.foxminded.booking.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @AfterAll
    @Sql("/test/sql/cleanTables.sql")
    static void cleanUp() {

    }

    @Test
    void shouldPersistInstance() {
        User expected = new User("a", "a", "a", "a",
                new Role(1l, "ROLE_ADMIN"));
        assertEquals(expected, repository.save(expected));
    }

    @Test
    void shouldThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> repository.save(new User()));
    }

    @Test
    void shouldThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> repository.save(null));
    }

    @Test
    void shouldFindById() {
        User expected = new User(1l,
                "Daniil",
                "Malashev",
                "daniil",
                "admin",
                new Role(1l, "ROLE_ADMIN"));
        assertEquals(expected, repository.findById(1l).get());
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyOptionalById() {
        assertEquals(Optional.empty(), repository.findById(1000l));
    }

    @Test
    void shouldFindByUsername() {
        User expected = new User(1l,
                "Daniil",
                "Malashev",
                "daniil",
                "admin",
                new Role(1l, "ROLE_ADMIN"));
        assertEquals(expected, repository.findByUsername("daniil").get());
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyOptionalByUsername() {
        assertEquals(Optional.empty(), repository.findByUsername("anyName"));
    }

    @Test
    void shouldFindAll() {
        List<User> expected = new ArrayList<User>() {{
            add(new User("Daniil", "Malashev", "daniil", "admin",
                    new Role(1l, "ROLE_ADMIN")));
            add(new User("Lewis", "Scott", "lewis", "user",
                    new Role(2l, "ROLE_USER")));
            add(new User("Edward", "Crock", "edward", "user",
                    new Role(2l, "ROLE_USER")));
            add(new User("Mathew", "Allen", "mathew", "user",
                    new Role(2l, "ROLE_USER")));
            add(new User("Hope", "Holder", "hope", "user",
                    new Role(2l, "ROLE_USER")));
            add(new User("Sophie", "McCartney", "sophie", "user",
                    new Role(2l, "ROLE_USER")));
            add(new User("Clare", "Grey", "clare", "user",
                    new Role(2l, "ROLE_USER")));
            add(new User("Nick", "Stanford", "nick", "user",
                    new Role(2l, "ROLE_USER")));
            add(new User("Claus", "Bridge", "claus", "user",
                    new Role(2l, "ROLE_USER")));
            add(new User("Sophie", "Rose", "sophie1", "user",
                    new Role(2l, "ROLE_USER")));
            add(new User("Peter", "Norton", "peter", "user",
                    new Role(2l, "ROLE_USER")));
            add(new User("Harry", "Potter", "harry", "user",
                    new Role(2l, "ROLE_USER")));
        }};
        assertEquals(expected, repository.findAll());
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyList() {
        List<User> expected = new ArrayList<>();
        assertEquals(expected, repository.findAll());
    }

    @Test
    void shouldDeleteById() {
        assertEquals(Optional.of(new User(1l,
                "Daniil",
                "Malashev",
                "daniil",
                "admin",
                new Role(1l, "ROLE_ADMIN"))), repository.findById(1l));
        repository.deleteById(1l);
        assertEquals(Optional.empty(), repository.findById(1l));
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldDeleteNonExistedInstance() {
        assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById(1000l));
    }
}
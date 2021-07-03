package com.foxminded.booking.repository;

import com.foxminded.booking.model.Role;
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
class RoleRepositoryTest {
    @Autowired
    private RoleRepository repository;

    @AfterAll
    @Sql("/test/sql/cleanTables.sql")
    static void cleanUp() {

    }

    @Test
    void shouldPersistInstance() {
        Role expected = new Role("a");
        assertEquals(expected, repository.save(expected));
    }

    @Test
    void shouldThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> repository.save(new Role()));
    }

    @Test
    void shouldThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> repository.save(null));
    }

    @Test
    void shouldFindById() {
        Role expected = new Role(1l, "ROLE_ADMIN");
        assertEquals(expected, repository.findById(1l).get());
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyOptionalById() {
        assertEquals(Optional.empty(), repository.findById(1000l));
    }

    @Test
    void shouldFindByName() {
        Role expected = new Role(1l, "ROLE_ADMIN");
        assertEquals(expected, repository.findByName("ROLE_ADMIN").get());
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyOptionalByName() {
        assertEquals(Optional.empty(), repository.findByName("ANY_ROLE"));
    }

    @Test
    void shouldFindAll() {
        List<Role> expected = new ArrayList<Role>() {{
            add(new Role("ROLE_ADMIN"));
            add(new Role("ROLE_USER"));
        }};
        assertEquals(expected, repository.findAll());
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyList() {
        List<Role> expected = new ArrayList<>();
        assertEquals(expected, repository.findAll());
    }

    @Test
    void shouldDeleteById() {
        assertEquals(Optional.of(new Role(1l, "ROLE_ADMIN")), repository.findById(1l));
        repository.deleteById(1l);
        assertEquals(Optional.empty(), repository.findById(1l));
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldDeleteNonExistedInstance() {
        assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById(1000l));
    }
}
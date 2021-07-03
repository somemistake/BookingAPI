package com.foxminded.booking.repository;

import com.foxminded.booking.model.Guide;
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
class GuideRepositoryTest {
    @Autowired
    private GuideRepository repository;

    @AfterAll
    @Sql("/test/sql/cleanTables.sql")
    static void cleanUp() {

    }

    @Test
    void shouldPersistInstance() {
        Guide expected = new Guide("a");
        assertEquals(expected, repository.save(expected));
    }

    @Test
    void shouldThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> repository.save(new Guide()));
    }

    @Test
    void shouldThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> repository.save(null));
    }

    @Test
    void shouldFindById() {
        Guide expected = new Guide(1l, "Chris");
        assertEquals(expected, repository.findById(1l).get());
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyOptionalById() {
        assertEquals(Optional.empty(), repository.findById(1000l));
    }

    @Test
    void shouldFindAll() {
        List<Guide> expected = new ArrayList<Guide>() {{
            add(new Guide("Chris"));
            add(new Guide("Bill"));
            add(new Guide("Kale"));
        }};
        assertEquals(expected, repository.findAll());
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyList() {
        List<Guide> expected = new ArrayList<>();
        assertEquals(expected, repository.findAll());
    }

    @Test
    void shouldDeleteById() {
        assertEquals(Optional.of(new Guide(1l, "Chris")), repository.findById(1l));
        repository.deleteById(1l);
        assertEquals(Optional.empty(), repository.findById(1l));
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldDeleteNonExistedInstance() {
        assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById(1000l));
    }
}
package com.foxminded.booking.repository;

import com.foxminded.booking.model.Tour;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class TourRepositoryTest {
    @Autowired
    private TourRepository repository;

    @AfterAll
    @Sql("/test/sql/cleanTables.sql")
    static void cleanUp() {

    }

    @Test
    void shouldPersistInstance() {
        Tour expected = new Tour(6l, "a", LocalDate.of(2022, 4, 1),
                LocalDate.of(2022, 4, 2));
        assertEquals(expected, repository.save(expected));
    }

    @Test
    void shouldThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> repository.save(new Tour()));
    }

    @Test
    void shouldThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> repository.save(null));
    }

    @Test
    void shouldFindById() {
        Tour expected = new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                LocalDate.of(2021, 4, 2));
        assertEquals(expected, repository.findById(1l).get());
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyOptionalById() {
        assertEquals(Optional.empty(), repository.findById(1000l));
    }

    @Test
    void shouldFindAll() {
        List<Tour> expected = new ArrayList<Tour>() {{
            add(new Tour(3l, "easy", LocalDate.of(2021, 4, 1),
                    LocalDate.of(2021, 4, 2)));
            add(new Tour(4l, "medium", LocalDate.of(2021, 4, 1),
                    LocalDate.of(2021, 4, 2)));
            add(new Tour(5l, "hard", LocalDate.of(2021, 4, 1),
                    LocalDate.of(2021, 4, 2)));
            add(new Tour(3l, "easy", LocalDate.of(2021, 5, 1),
                    LocalDate.of(2021, 5, 2)));
            add(new Tour(4l, "medium", LocalDate.of(2021, 5, 1),
                    LocalDate.of(2021, 5, 2)));
            add(new Tour(5l, "hard", LocalDate.of(2021, 5, 1),
                    LocalDate.of(2021, 5, 2)));
        }};
        assertEquals(expected, repository.findAll());
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyList() {
        List<Tour> expected = new ArrayList<>();
        assertEquals(expected, repository.findAll());
    }

    @Test
    void shouldDeleteById() {
        assertEquals(Optional.of(new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                LocalDate.of(2021, 4, 2))), repository.findById(1l));
        repository.deleteById(1l);
        assertEquals(Optional.empty(), repository.findById(1l));
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldDeleteNonExistedInstance() {
        assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById(1000l));
    }
}
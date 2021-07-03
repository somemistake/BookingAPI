package com.foxminded.booking.repository;

import com.foxminded.booking.model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository repository;

    @AfterAll
    @Sql("/test/sql/cleanTables.sql")
    static void cleanUp() {

    }

    @Test
    void shouldPersistInstance() {
        Booking expected = new Booking(
                new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                        LocalDate.of(2021, 4, 2)),
                new User(5l, "Hope", "Holder", "hope", "user", new Role("ROLE_USER")),
                new Guide(1l, "Chris")
        );
        assertEquals(expected, repository.save(expected));
    }

    @Test
    void shouldThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> repository.save(null));
    }

    @Test
    void shouldFindById() {
        Booking expected = new Booking(
                1l,
                new Tour(3l, "easy", LocalDate.of(2021, 4, 1),
                        LocalDate.of(2021, 4, 2)),
                new User("Lewis", "Scott", "lewis", "user", new Role("ROLE_USER")),
                new Guide("Chris")
        );
        assertEquals(expected, repository.findById(1l).get());
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyOptionalById() {
        assertEquals(Optional.empty(), repository.findById(1000l));
    }

    @Test
    void shouldFindByTourId() {
        List<Booking> expected = new ArrayList<Booking>() {{
            add(new Booking(
                    new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                            LocalDate.of(2021, 4, 2)),
                    new User(2l, "Lewis", "Scott", "lewis", "user", new Role("ROLE_USER")),
                    new Guide(1l, "Chris")
            ));
            add(new Booking(
                    new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                            LocalDate.of(2021, 4, 2)),
                    new User(3l, "Edward", "Crock", "edward", "user", new Role("ROLE_USER")),
                    new Guide(1l, "Chris")
            ));
            add(new Booking(
                    new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                            LocalDate.of(2021, 4, 2)),
                    new User(4l, "Mathew", "Allen", "mathew", "user", new Role("ROLE_USER")),
                    new Guide(1l, "Chris")
            ));
        }};
        assertEquals(expected, repository.findByTourId(1l));
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyListByTourId() {
        assertEquals(new ArrayList<>(), repository.findByTourId(1l));
    }

    @Test
    void shouldFindByUserId() {
        List<Booking> expected = new ArrayList<Booking>() {{
            add(new Booking(
                    new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                            LocalDate.of(2021, 4, 2)),
                    new User(2l, "Lewis", "Scott", "lewis", "user", new Role("ROLE_USER")),
                    new Guide(1l, "Chris")
            ));
        }};
        assertEquals(expected, repository.findByUserId(2l));
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyListByUserId() {
        assertEquals(new ArrayList<>(), repository.findByUserId(2l));
    }

    @Test
    void shouldFindByGuideId() {
        List<Booking> expected = new ArrayList<Booking>() {{
            add(new Booking(
                    new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                            LocalDate.of(2021, 4, 2)),
                    new User(2l, "Lewis", "Scott", "lewis", "user", new Role("ROLE_USER")),
                    new Guide(1l, "Chris")
            ));
            add(new Booking(
                    new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                            LocalDate.of(2021, 4, 2)),
                    new User(3l, "Edward", "Crock", "edward", "user", new Role("ROLE_USER")),
                    new Guide(1l, "Chris")
            ));
            add(new Booking(
                    new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                            LocalDate.of(2021, 4, 2)),
                    new User(4l, "Mathew", "Allen", "mathew", "user", new Role("ROLE_USER")),
                    new Guide(1l, "Chris")
            ));
        }};
        assertEquals(expected, repository.findByGuideId(1l));
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyListByGuideId() {
        assertEquals(new ArrayList<>(), repository.findByGuideId(1l));
    }

    @Test
    void shouldFindAll() {
        List<Booking> expected = new ArrayList<Booking>() {{
            add(new Booking(
                    new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                            LocalDate.of(2021, 4, 2)),
                    new User(2l, "Lewis", "Scott", "lewis", "user", new Role("ROLE_USER")),
                    new Guide(1l, "Chris")
            ));
            add(new Booking(
                    new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                            LocalDate.of(2021, 4, 2)),
                    new User(3l, "Edward", "Crock", "edward", "user", new Role("ROLE_USER")),
                    new Guide(1l, "Chris")
            ));
            add(new Booking(
                    new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                            LocalDate.of(2021, 4, 2)),
                    new User(4l, "Mathew", "Allen", "mathew", "user", new Role("ROLE_USER")),
                    new Guide(1l, "Chris")
            ));
        }};
        assertEquals(expected, repository.findAll());
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldFindEmptyList() {
        List<Booking> expected = new ArrayList<>();
        assertEquals(expected, repository.findAll());
    }

    @Test
    void shouldDeleteById() {
        assertEquals(Optional.of(new Booking(
                1l,
                new Tour(3l, "easy", LocalDate.of(2021, 4, 1),
                        LocalDate.of(2021, 4, 2)),
                new User("Lewis", "Scott", "lewis", "user", new Role("ROLE_USER")),
                new Guide("Chris")
        )), repository.findById(1l));
        repository.deleteById(1l);
        assertEquals(Optional.empty(), repository.findById(1l));
    }

    @Test
    @Sql("/test/sql/cleanTables.sql")
    void shouldDeleteNonExistedInstance() {
        assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById(1000l));
    }
}
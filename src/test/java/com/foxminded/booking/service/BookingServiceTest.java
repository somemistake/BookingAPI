package com.foxminded.booking.service;

import com.foxminded.booking.model.Booking;
import com.foxminded.booking.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookingServiceTest {
    @MockBean
    private BookingRepository repository;

    @Autowired
    private BookingService service;

    @Test
    void shouldFindById() {
        Optional<Booking> expected = Optional.of(new Booking());
        when(repository.findById(1l)).thenReturn(expected);
        assertEquals(expected, service.findById(1l));
    }

    @Test
    void shouldFindByTourId() {
        List<Booking> expected = new ArrayList<>();
        when(repository.findByTourId(1l)).thenReturn(expected);
        assertEquals(expected, service.findByTourId(1l));
    }

    @Test
    void shouldFindByUserId() {
        List<Booking> expected = new ArrayList<>();
        when(repository.findByUserId(1l)).thenReturn(expected);
        assertEquals(expected, service.findByUserId(1l));
    }

    @Test
    void shouldFindByGuideId() {
        List<Booking> expected = new ArrayList<>();
        when(repository.findByGuideId(1l)).thenReturn(expected);
        assertEquals(expected, service.findByGuideId(1l));
    }

    @Test
    void shouldFindAll() {
        List<Booking> expected = new ArrayList<>();
        when(repository.findAll()).thenReturn(expected);
        assertEquals(expected, service.findAll());
    }

    @Test
    void shouldPersistInstance() {
        Booking expected = new Booking();
        when(repository.save(expected)).thenReturn(expected);
        assertEquals(expected, service.save(expected));
    }

    @Test
    void shouldDeleteById() {
        doNothing().when(repository).deleteById(1l);
        service.deleteById(1l);
        verify(repository, times(1)).deleteById(1l);
    }
}
package com.foxminded.booking.service;

import com.foxminded.booking.model.Tour;
import com.foxminded.booking.repository.TourRepository;
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
class TourServiceTest {
    @MockBean
    private TourRepository repository;

    @Autowired
    private TourService service;

    @Test
    void shouldFindById() {
        Optional<Tour> expected = Optional.of(new Tour());
        when(repository.findById(1l)).thenReturn(expected);
        assertEquals(expected, service.findById(1l));
    }

    @Test
    void shouldFindAll() {
        List<Tour> expected = new ArrayList<>();
        when(repository.findAll()).thenReturn(expected);
        assertEquals(expected, service.findAll());
    }

    @Test
    void shouldPersistInstance() {
        Tour expected = new Tour();
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
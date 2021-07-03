package com.foxminded.booking.service;

import com.foxminded.booking.model.Tour;
import com.foxminded.booking.repository.TourRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TourService {
    private final TourRepository repository;

    public TourService(TourRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<Tour> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public List<Tour> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Tour save(Tour tour) {
        return repository.save(tour);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

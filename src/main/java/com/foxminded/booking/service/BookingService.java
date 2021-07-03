package com.foxminded.booking.service;

import com.foxminded.booking.model.Booking;
import com.foxminded.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<Booking> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public List<Booking> findByTourId(Long tourId) {
        return repository.findByTourId(tourId);
    }

    @Transactional
    public List<Booking> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    @Transactional
    public List<Booking> findByGuideId(Long guideId) {
        return repository.findByGuideId(guideId);
    }

    @Transactional
    public List<Booking> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Booking save(Booking booking) {
        return repository.save(booking);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

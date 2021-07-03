package com.foxminded.booking.service;

import com.foxminded.booking.model.Guide;
import com.foxminded.booking.repository.GuideRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class GuideService {
    private final GuideRepository repository;

    public GuideService(GuideRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<Guide> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public List<Guide> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Guide save(Guide guide) {
        return repository.save(guide);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

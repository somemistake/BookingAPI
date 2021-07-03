package com.foxminded.booking.service;

import com.foxminded.booking.model.Role;
import com.foxminded.booking.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<Role> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Optional<Role> findByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    public List<Role> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Role save(Role role) {
        return repository.save(role);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

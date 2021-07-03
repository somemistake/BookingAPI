package com.foxminded.booking.service;

import com.foxminded.booking.model.User;
import com.foxminded.booking.repository.UserRepository;
import com.foxminded.booking.security.jwt.JwtUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Transactional
    public List<User> findAll() {
        return repository.findAll();
    }

    @Transactional
    public User save(User user) {
        return repository.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = findByUsername(username);
        if (user.isPresent())
            return new JwtUser(
                    user.get().getFirstName(),
                    user.get().getLastName(),
                    user.get().getUsername(),
                    user.get().getPassword(),
                    new ArrayList<SimpleGrantedAuthority>() {{
                        add(new SimpleGrantedAuthority(user.get().getRole().getName()));
                    }}
            );
        else
            throw new UsernameNotFoundException(String.format("User with '%s' username not found", username));
    }
}

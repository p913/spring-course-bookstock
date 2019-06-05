package com.pvil.otuscourse.bookstockmvcformlogin.service;

import com.pvil.otuscourse.bookstockmvcformlogin.domain.User;
import com.pvil.otuscourse.bookstockmvcformlogin.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}

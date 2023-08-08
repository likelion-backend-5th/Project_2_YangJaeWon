package com.example.project2.service;

import com.example.project2.entity.User;
import com.example.project2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User readUser(String userName) {
        if (userRepository.findByUsername(userName).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return userRepository.findByUsername(userName).get();
    }


}

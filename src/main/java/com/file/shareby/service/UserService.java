package com.file.shareby.service;

import com.file.shareby.domain.User;
import com.file.shareby.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity registerUser(User user) {
        log.debug("user registration");
        Optional<User> availableUser = null;
        if (Objects.nonNull(user) && StringUtils.hasText(user.getEmail())) {
            availableUser = userRepository.findByEmail(user.getEmail());
        }
        if (availableUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

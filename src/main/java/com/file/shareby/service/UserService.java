package com.file.shareby.service;

import com.file.shareby.domain.User;
import com.file.shareby.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity registerUser(User user,HttpSession httpSession) {
        log.debug("user registration");
        if (Objects.nonNull(user) && StringUtils.hasText(user.getEmail())) {
            Optional<User> availableUser = userRepository.findByEmail(user.getEmail());
            if (availableUser.isPresent()) {
                httpSession.setAttribute("user", availableUser.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        User userData = userRepository.save(user);
        httpSession.setAttribute("user",userData);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

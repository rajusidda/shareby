package com.file.shareby.service;

import com.file.shareby.customexception.InvalidUserDataException;
import com.file.shareby.domain.User;
import com.file.shareby.mapping.UserDataMapper;
import com.file.shareby.payload.UserDTO;
import com.file.shareby.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserDataMapper userDataMapper;

    public UserDTO registerUser(UserDTO userDTO, HttpSession httpSession) {
        log.debug("user registration");
        User user = userDataMapper.mapDtoToDomain(userDTO);
        if (Objects.nonNull(user) && StringUtils.hasText(user.getEmail())) {
            Optional<User> availableUser = userRepository.findByEmail(user.getEmail());
            if (availableUser.isPresent()) {
                httpSession.setAttribute("user", availableUser.get());
                return userDataMapper.mapDomainToDto(availableUser.get());
            }
            User userData = userRepository.save(user);
            httpSession.setAttribute("user", userData);
            return userDataMapper.mapDomainToDto(userData);
        }
        throw new InvalidUserDataException("you have provided invalid data for register");
    }

}

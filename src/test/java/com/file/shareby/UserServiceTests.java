package com.file.shareby;

import com.file.shareby.customexception.InvalidUserDataException;
import com.file.shareby.domain.User;
import com.file.shareby.mapping.UserDataMapper;
import com.file.shareby.payload.UserDTO;
import com.file.shareby.repository.UserRepository;
import com.file.shareby.service.UserService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSession httpSession;

    @Mock
    private UserDataMapper userDataMapper;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void signUpTest(){
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(getUserData()));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(getUserData());
        Mockito.when(userDataMapper.mapDtoToDomain(Mockito.any())).thenReturn(getUserData());
        Mockito.when(userDataMapper.mapDomainToDto(Mockito.any())).thenReturn(getUserDataDTO());
        UserDTO userDTO = userService.registerUser(getUserDataDTO(), httpSession);
        Assert.assertNotNull(userDTO);
    }

    @Test
    public void registerUserTest(){
        Mockito.when(userDataMapper.mapDtoToDomain(Mockito.any())).thenReturn(getUserData());
        Mockito.when(userDataMapper.mapDomainToDto(Mockito.any())).thenReturn(getUserDataDTO());
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(getUserData());
        UserDTO userDTO = userService.registerUser(getUserDataDTO(), httpSession);
        Assert.assertNotNull(userDTO);
    }
    @Test
    public void registerUserExceptionTest(){
        exceptionRule.expect(InvalidUserDataException.class);
        Mockito.when(userDataMapper.mapDtoToDomain(Mockito.any())).thenReturn(getUserWithoutEmail());
        Mockito.when(userDataMapper.mapDomainToDto(Mockito.any())).thenReturn(getUserDataDTO());
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(getUserData());
        userService.registerUser(getUserDataDTO(), httpSession);
    }


    public User getUserData(){
        User user = new User();
        user.setEmail("raj@gmail.com");
        return user;
    }
    public User getUserWithoutEmail(){
        User user = new User();
        return user;
    }
    public UserDTO getUserDataDTO(){
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("raj@gmail.com");
        return userDTO;
    }
}

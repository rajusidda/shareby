package com.file.shareby;

import com.file.shareby.domain.User;
import com.file.shareby.repository.UserRepository;
import com.file.shareby.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Test
    public void signUpTest(){
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(getUserData()));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(getUserData());
        ResponseEntity responseEntity = userService.registerUser(getUserData(), httpSession);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(responseEntity.getStatusCode().value(),200);
    }

    @Test
    public void registerUserTest(){
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(getUserData());
        ResponseEntity responseEntity = userService.registerUser(getUserData(), httpSession);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(responseEntity.getStatusCode().value(),201);
    }

    @Test
    public  void registerUserWithoutEmailTest(){
        User user= new User();
        ResponseEntity responseEntity = userService.registerUser(user, httpSession);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(responseEntity.getStatusCode().value(),400);
    }

    public User getUserData(){
        User user = new User();
        user.setEmail("raj@gmail.com");
        return user;
    }
}

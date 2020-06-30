package com.file.shareby;

import com.file.shareby.customexception.FileNotFoundException;
import com.file.shareby.customexception.FileStorageException;
import com.file.shareby.domain.SharedData;
import com.file.shareby.domain.UploadData;
import com.file.shareby.domain.User;
import com.file.shareby.mapping.SharedDataMapper;
import com.file.shareby.mapping.UploadDataMapper;
import com.file.shareby.payload.FilesDTO;
import com.file.shareby.payload.SharedDataDTO;
import com.file.shareby.repository.SharedDataRepository;
import com.file.shareby.repository.UploadDataRepository;
import com.file.shareby.repository.UserRepository;
import com.file.shareby.service.SharedbyService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SharedbyServiceTests {

    @InjectMocks
    private SharedbyService sharedbyService;

    @Mock
    private SharedDataRepository sharedDataRepository;

    @Mock
    private UploadDataRepository uploadDataRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UploadDataMapper uploadDataMapper;

    @Mock
    private SharedDataMapper sharedDataMapper;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void uploadedFileTest() throws Exception {
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.png");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test", "image/png", inputStream);
        Mockito.when(uploadDataRepository.save(Mockito.any())).thenReturn(getUploadedData());
        UploadData uploadFile = sharedbyService.uploadFile(multipartFile, getUserData());
        Assert.assertNotNull(uploadFile);
    }
    @Test
    public void uploadedFileExceptionTest() throws Exception {
        exceptionRule.expect(FileStorageException.class);
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.png");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test", "image/png", inputStream);
        Mockito.when(uploadDataRepository.save(Mockito.any())).thenReturn(getUploadedDataWithoutId());
        sharedbyService.uploadFile(multipartFile, getUserData());
    }

    @Test
    public void shareFileTest() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(getUserData()));
        Mockito.when(uploadDataRepository.findById(getSharedData().getFileId())).thenReturn(Optional.of(getUploadedData()));
        Mockito.when(sharedDataRepository.save(getSharedData())).thenReturn(getSharedData());
        Mockito.when(sharedDataMapper.mapDtoToDomain(Mockito.any())).thenReturn(getSharedData());
        Mockito.when(sharedDataMapper.mapDomainToDto(getSharedData())).thenReturn(getSharedDataDTO());
        SharedDataDTO sharedDataDTO = sharedbyService.shareFile(getSharedDataDTO(), getUserData());
        Assert.assertNotNull(sharedDataDTO);
    }
    @Test
    public void shareFileExceptionTest() {
        exceptionRule.expect(FileNotFoundException.class);
        Mockito.when(sharedDataMapper.mapDtoToDomain(Mockito.any())).thenReturn(getSharedData());
        Mockito.when(sharedDataMapper.mapDomainToDto(getSharedData())).thenReturn(getSharedDataDTO());
        sharedbyService.shareFile(getSharedDataDTO(), getUserData());
    }

    @Test
    public void getFilesTest() {
        List<UploadData> uploadDataList = new ArrayList<>();
        uploadDataList.add(getUploadedData());
        List<SharedData> sharedDataList = new ArrayList<>();
        sharedDataList.add(getSharedData());
        Mockito.when(uploadDataRepository.findByUser(Mockito.any())).thenReturn(uploadDataList);
        Mockito.when(sharedDataRepository.findByToUsers(Mockito.any())).thenReturn(sharedDataList);
        Mockito.when(sharedDataRepository.save(Mockito.any())).thenReturn(getSharedData());
        FilesDTO files = sharedbyService.getFiles(getUserData());
        Assert.assertNotNull(files);
    }

    public UploadData getUploadedData() {
        UploadData uploadData = new UploadData();
        uploadData.setFileName("file");
        uploadData.setFileType("txt/plain");
        uploadData.setUser(getUserData());
        uploadData.setId("12345");
        return uploadData;
    }
    public UploadData getUploadedDataWithoutId() {
        UploadData uploadData = new UploadData();
        uploadData.setFileName("file");
        uploadData.setFileType("txt/plain");
        uploadData.setUser(getUserData());
        return uploadData;
    }

    public SharedData getSharedData() {
        List<User> userList = new ArrayList<>();
        userList.add(getUserData());
        SharedData sharedData = new SharedData();
        sharedData.setFileId("12345");
        sharedData.setToUsers(userList);
        return sharedData;
    }

    public SharedDataDTO getSharedDataDTO() {
        List<User> userList = new ArrayList<>();
        userList.add(getUserData());
        SharedDataDTO sharedDataDTO = new SharedDataDTO();
        sharedDataDTO.setFileId("12345");
        sharedDataDTO.setToUsers(userList);
        sharedDataDTO.setFileBy("shared");
        sharedDataDTO.setFromUser("raj@gmail.com");
        sharedDataDTO.setId("1234");
        return sharedDataDTO;
    }
    public SharedDataDTO getSharedDataDTOWithoutEmail() {
        List<User> userList = new ArrayList<>();
        userList.add(getUserData());
        SharedDataDTO sharedDataDTO = new SharedDataDTO();
        sharedDataDTO.setFileId("12345");
        sharedDataDTO.setToUsers(userList);
        return sharedDataDTO;
    }

    public User getUserData() {
        User user = new User();
        user.setEmail("raj@gmail.com");
        user.setId("123");
        user.setPassword("2323");
        return user;
    }


}

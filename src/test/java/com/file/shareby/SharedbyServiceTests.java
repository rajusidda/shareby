package com.file.shareby;

import com.file.shareby.domain.SharedData;
import com.file.shareby.domain.UploadData;
import com.file.shareby.domain.User;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
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

    //@Test
    public void downloadFileTest() throws Exception {
        exceptionRule.expect(NullPointerException.class);
        Mockito.when(uploadDataRepository.findById(Mockito.anyString())).thenReturn(Optional.of(getUploadedData()));
        Mockito.when(sharedDataRepository.findByFileId(Mockito.anyString())).thenReturn(Optional.of(getSharedData()));
        Mockito.when(new String((byte[]) Mockito.any())).thenReturn("test.txt");
        String downloadFile = sharedbyService.downloadFile("12345", getUserData());
        Assert.assertNotNull(downloadFile);
    }

    @Test
    public void shareFileTest() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(getUserData()));
        Mockito.when(uploadDataRepository.findById(getSharedData().getFileId())).thenReturn(Optional.of(getUploadedData()));
        Mockito.when(sharedDataRepository.save(getSharedData())).thenReturn(getSharedData());
        ResponseEntity responseEntity = sharedbyService.shareFile(getSharedData(), getUserData());
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(responseEntity.getStatusCode().value(),200);
    }
    @Test
    public void shareFileForbiddenTest() {
        ResponseEntity responseEntity = sharedbyService.shareFile(getSharedData(), getUserData());
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(responseEntity.getStatusCode().value(),403);
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
        ResponseEntity<Object> responseEntity = sharedbyService.getFiles(getUserData());
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(responseEntity.getStatusCode().value(),200);
    }
    @Test
    public void getFilesNotAvailableTest() {
        List<UploadData> uploadDataList = new ArrayList<>();
        uploadDataList.add(getUploadedData());
        ResponseEntity<Object> responseEntity = sharedbyService.getFiles(getUserData());
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(responseEntity.getStatusCode().value(),404);
    }

    public UploadData getUploadedData() {
        UploadData uploadData = new UploadData();
        uploadData.setFileName("file");
        uploadData.setFileType("txt/plain");
        uploadData.setUser(getUserData());
        uploadData.setId("12345");
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

    public User getUserData() {
        User user = new User();
        user.setEmail("raj@gmail.com");
        return user;
    }


}

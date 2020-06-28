package com.file.shareby.service;

import com.file.shareby.domain.SharedData;
import com.file.shareby.domain.UploadData;
import com.file.shareby.domain.User;
import com.file.shareby.payload.Response;
import com.file.shareby.repository.FileSharingRepository;
import com.file.shareby.repository.FileStorageRepository;
import com.file.shareby.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class SharedbyService {

    public static final String OWNED = "owned";
    public static final String SHARED = "shared";
    public static User loginUser;

    @Value("${file.upload-path}")
    private String FILE_UPLOAD_PATH;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private FileSharingRepository fileSharingRepository;

    public ResponseEntity registerUser(User user) {
        log.debug("user registration");
        Optional<User> availableUser = null;
        if (Objects.nonNull(user) && StringUtils.hasText(user.getEmail())) {
            availableUser = userRepository.findByEmail(user.getEmail());
        }
        if (availableUser.isPresent()) {
            User user1 = new User();
            user1.setEmail(availableUser.get().getEmail());
            user1.setId(availableUser.get().getId());
            loginUser = user1;
            Response response = Response.builder()
                    .email(availableUser.get().getEmail())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User userdata = userRepository.save(user);
        User user2 = new User();
        user2.setEmail(userdata.getEmail());
        user2.setId(userdata.getId());
        loginUser = user2;
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public UploadData uploadFile(MultipartFile file) throws Exception {

        String filename = file.getOriginalFilename() + LocalDateTime.now();
        file.transferTo(new File(FILE_UPLOAD_PATH + filename));
        UploadData uploadData = new UploadData();
        uploadData.setFileName(filename);
        uploadData.setFileType(file.getContentType());
        uploadData.setUser(loginUser);
        UploadData uploadData1 = fileStorageRepository.save(uploadData);

        return uploadData1;

    }

    public String downloadFile(String id) throws IOException {
        Optional<UploadData> fileData = fileStorageRepository.findById(id);
        Optional<SharedData> sharedData = fileSharingRepository.findByFileId(id);
        if (fileData.isPresent() || sharedData.isPresent()
                && fileData.get().getUser().getEmail().equals(loginUser.getEmail())) {
            return new String(Files.readAllBytes(Paths.get(FILE_UPLOAD_PATH + fileData.get().getFileName())));
        }
        return null;

    }

    public ResponseEntity shareFile(SharedData sharedData) {

        Optional<UploadData> fileData = fileStorageRepository.findById(sharedData.getFileId());
        if (fileData.isPresent() && fileData.get().getUser().getEmail().equals(loginUser.getEmail())) {
            SharedData sharedDataData = fileSharingRepository.save(sharedData);
            /*Response response = Response.builder()
                                        .fileID(sharedDataData.getFileId())
                                        .email(sharedDataData.getUserEmail())
                                        .build();*/
            return new ResponseEntity<>(sharedDataData, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity getFiles() {

        List<Object> objectList = new ArrayList<>();
        List<UploadData> uploadDataList = fileStorageRepository.findByUser(loginUser).stream()
                .map(uploadData -> {
                    uploadData.setFileBy(OWNED);
                    return uploadData;
                }).collect(Collectors.toList());

        List<SharedData> sharedDataList = fileSharingRepository.findByToUsers(loginUser).stream()
                .map(sharedData -> {
                    sharedData.setFileBy(SHARED);
                    return sharedData;
                })
                .collect(Collectors.toList());
        objectList.add(uploadDataList);
        objectList.add(sharedDataList);

        return new ResponseEntity<>(objectList, HttpStatus.OK);
    }


}

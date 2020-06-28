package com.file.shareby.service;

import com.file.shareby.domain.SharedData;
import com.file.shareby.domain.UploadData;
import com.file.shareby.domain.User;
import com.file.shareby.repository.SharedDataRepository;
import com.file.shareby.repository.UploadDataRepository;
import com.file.shareby.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class SharedbyService {

    public static final String OWNED = "owned";
    public static final String SHARED = "shared";

    @Value("${file.upload-path}")
    private String FILE_UPLOAD_PATH;

    @Autowired
    private UploadDataRepository uploadDataRepository;

    @Autowired
    private SharedDataRepository sharedDataRepository;

    @Autowired
    private UserRepository userRepository;


    public UploadData uploadFile(MultipartFile file, User user) throws Exception {

        String filename = file.getOriginalFilename() + LocalDateTime.now();
        file.transferTo(new File(FILE_UPLOAD_PATH + filename));
        UploadData data = new UploadData();
        data.setFileName(filename);
        data.setFileType(file.getContentType());
        data.setUser(user);
        UploadData uploadData = uploadDataRepository.save(data);

        return uploadData;

    }

    public String downloadFile(String id, User user) throws IOException {
        Optional<UploadData> fileData = uploadDataRepository.findById(id);
        Optional<SharedData> sharedData = sharedDataRepository.findByFileId(id);
        if (fileData.isPresent() || sharedData.isPresent()
                && fileData.get().getUser().getEmail().equals(user.getEmail())) {
            return new String(Files.readAllBytes(Paths.get(FILE_UPLOAD_PATH + fileData.get().getFileName())));
        }
        return null;

    }

    public ResponseEntity shareFile(SharedData sharedData, User user) {
        List<User> userList = sharedData.getToUsers().stream()
                .map(sharedDataData -> userRepository.findByEmail(sharedDataData.getEmail()))
                .filter(userData -> userData.isPresent())
                .map(userDetails -> userDetails.get())
                .collect(Collectors.toList());
        sharedData.setToUsers(userList);
        Optional<UploadData> fileData = uploadDataRepository.findById(sharedData.getFileId());
        if (fileData.isPresent() && fileData.get().getUser().getEmail().equals(user.getEmail())) {
            SharedData sharedDataData = sharedDataRepository.save(sharedData);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity getFiles(User user) {

        List<Object> objectList = new ArrayList<>();
        List<UploadData> uploadDataList = uploadDataRepository.findByUser(user).stream()
                .map(uploadData -> {
                    uploadData.setFileBy(OWNED);
                    return uploadData;
                }).collect(Collectors.toList());

        List<SharedData> sharedDataList = sharedDataRepository.findByToUsers(user).stream()
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

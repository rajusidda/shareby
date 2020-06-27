package com.file.shareby.service;

import com.file.shareby.domain.FileData;
import com.file.shareby.domain.SharedData;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class SharedbyService {

    public static final String OWNED = "owned";
    public static final String SHARED = "shared";
    public static String email;

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
        if(Objects.nonNull(user) && StringUtils.hasText(user.getEmail())){
            availableUser = userRepository.findByEmail(user.getEmail());
        }
        if(availableUser.isPresent()){

            /*Response response = Response.builder()
                                        .email(availableUser.get().getEmail())
                                        .build();*/
            return new ResponseEntity<>(availableUser.get().getEmail(), HttpStatus.OK);
        }
        User userdata = userRepository.save(user);
        email = userdata.getEmail();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public FileData uploadFile(MultipartFile file) throws IllegalStateException {
        String filename = file.getOriginalFilename();
        try {
            file.transferTo(new File(FILE_UPLOAD_PATH + file.getOriginalFilename()));
            FileData fileData = new FileData(filename,email,file.getContentType(), file.getBytes());
            return fileStorageRepository.save(fileData);
        }catch (Exception e){
                e.printStackTrace();
        }
        return null;
    }

    public FileData getFile(String id) {
        Optional<FileData> fileData = fileStorageRepository.findById(id);
        Optional<SharedData> sharedData = fileSharingRepository.findByFileId(id);
        if(fileData.isPresent() || sharedData.isPresent() && fileData.get().getEmail().equals(email)){
            return fileStorageRepository.findById(id).get();
        }
        return null;

    }

    public ResponseEntity shareFile(SharedData sharedData) {
        sharedData.setOwnerEmail(email);
        Optional<FileData> fileData = fileStorageRepository.findById(sharedData.getFileId());
        if(fileData.isPresent() && fileData.get().getEmail().equals(email)) {
            SharedData sharedDataData = fileSharingRepository.save(sharedData);
            Response response = Response.builder()
                                        .fileID(sharedDataData.getFileId())
                                        .email(sharedDataData.getUserEmail())
                                        .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity getFiles() {
        HashMap<String,String> map = new HashMap<>();
        fileStorageRepository.findAllByEmail(email)
                             .forEach(fileData -> map.put(fileData.getId(),OWNED));
        fileSharingRepository.findAllByUserEmail(email)
                             .forEach(sharedData -> map.put(sharedData.getFileId(),SHARED));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}

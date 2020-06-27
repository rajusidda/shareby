package com.file.shareby.service;

import com.file.shareby.domain.DataFile;
import com.file.shareby.domain.Share;
import com.file.shareby.domain.User;
import com.file.shareby.repository.FileSharingRepository;
import com.file.shareby.repository.FileStorageRepository;
import com.file.shareby.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class SharedbyService {

    public static final String OWNED = "owned";
    public static final String SHARED = "shared";
    public static String email;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileStorageRepository fileStorageRepository;

    @Autowired
    FileSharingRepository fileSharingRepository;

    @Autowired
    HttpSession httpSession;

    public ResponseEntity registerUser(User user) {

        log.debug("user registration");
        Optional<User> availableUser = null;
        if(Objects.nonNull(user) && StringUtils.hasText(user.getEmail())){
            availableUser = userRepository.findByEmail(user.getEmail());
        }
        if(availableUser.isPresent()){
            return new ResponseEntity<>(availableUser, HttpStatus.OK);
        }
        User userdata = userRepository.save(user);
        email = userdata.getEmail();
        return new ResponseEntity<User>(HttpStatus.CREATED);
    }

    public DataFile uploadFile(MultipartFile file) throws IllegalStateException {
        String filename = file.getOriginalFilename();
        try {
        DataFile dataFile = new DataFile(filename,email,file.getContentType(), file.getBytes());
        return fileStorageRepository.save(dataFile);
        }catch (Exception e){
                e.printStackTrace();
        }
        return null;
    }

    public DataFile getFile(String id) {
        Optional<DataFile> dataFile = fileStorageRepository.findById(id);
        Optional<Share> share = fileSharingRepository.findByFileId(id);
        if(dataFile.isPresent() || share.isPresent() && dataFile.get().getEmail().equals(email)){
            return fileStorageRepository.findById(id).get();
        }
        return null;

    }

    public ResponseEntity<Share> shareFile(Share share) {
        share.setOwnerEmial(email);
        Optional<DataFile> dataFile = fileStorageRepository.findById(share.getFileId());
        if(dataFile.isPresent() && dataFile.get().getEmail().equals(email)) {
            Share shareData = fileSharingRepository.save(share);
            return new ResponseEntity<>(shareData, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<?> getFiles() {
        HashMap<String,String> map = new HashMap<>();
        fileStorageRepository.findAllByEmail(email).forEach(dataFile -> map.put(dataFile.getId(),OWNED));
        fileSharingRepository.findAllByUserEmail(email).forEach(share -> map.put(share.getFileId(),SHARED));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}

package com.file.shareby.controller;

import com.file.shareby.domain.SharedData;
import com.file.shareby.domain.UploadData;
import com.file.shareby.domain.User;
import com.file.shareby.payload.UploadResponse;
import com.file.shareby.service.SharedbyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@RequestMapping("/api")
@Slf4j
@RestController
public class SharedbyController {

    @Autowired
    private SharedbyService sharedbyService;

    @PostMapping("/v1/file/upload")
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file,HttpSession httpSession) {
        try {
            User user = (User) httpSession.getAttribute("user");
            UploadData uploadData = sharedbyService.uploadFile(file,user);
            UploadResponse uploadResponse = new UploadResponse();
            uploadResponse.setFileID(uploadData.getId());
            return new ResponseEntity<>(uploadResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception occurred while saving the file");
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/v1/file/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable("id") String id,HttpSession httpSession) {
        try {
            User user = (User) httpSession.getAttribute("user");
            String file = sharedbyService.downloadFile(id,user);
            return new ResponseEntity<>(file, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Exception occurred while downloading file");
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/v1/file/share")
    public ResponseEntity<?> shreFile(@RequestBody SharedData sharedData,HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        return sharedbyService.shareFile(sharedData,user);
    }

    @GetMapping("/v1/file")
    public ResponseEntity<?> getFiles(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        return sharedbyService.getFiles(user);
    }

}

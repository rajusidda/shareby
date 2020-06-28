package com.file.shareby.controller;

import com.file.shareby.domain.SharedData;
import com.file.shareby.domain.UploadData;
import com.file.shareby.payload.UploadResponse;
import com.file.shareby.service.SharedbyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
@Slf4j
@RestController
public class SharedbyController {

    @Autowired
    private SharedbyService sharedbyService;

    @PostMapping("/v1/file/upload")
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            UploadData uploadData = sharedbyService.uploadFile(file);
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
    public ResponseEntity downloadFile(@PathVariable("id") String id) {
        try {
            String file = sharedbyService.downloadFile(id);
            return new ResponseEntity<>(file, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Exception occurred while downloading file");
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/v1/file/share")
    public ResponseEntity shreFile(@RequestBody SharedData sharedData) {
        return sharedbyService.shareFile(sharedData);
    }

    @GetMapping("/v1/file")
    public ResponseEntity getFiles() {
        return sharedbyService.getFiles();
    }

}

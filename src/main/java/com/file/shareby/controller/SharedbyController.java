package com.file.shareby.controller;

import com.file.shareby.domain.DataFile;
import com.file.shareby.domain.Share;
import com.file.shareby.domain.User;
import com.file.shareby.service.SharedbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class SharedbyController {

    @Autowired
    SharedbyService sharedbyService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user){
        return  sharedbyService.registerUser(user);
    }


    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        DataFile dataFile = sharedbyService.uploadFile(file);
        return new ResponseEntity<>(dataFile.getId(), HttpStatus.OK);
    }

    @GetMapping("/api/file/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable ("id") String id ){

        DataFile file = sharedbyService.getFile(id);
        if(file!=null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename=\"" + file.getFileName() + "\"")
                    .body(new ByteArrayResource(file.getData()));
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/api/share")
    public ResponseEntity<Share> shreFile(@RequestBody Share share){

        return sharedbyService.shareFile(share);
    }

    @GetMapping("/api/file")
    public ResponseEntity<?> getFiles(){
       return sharedbyService.getFiles();
    }


}

package com.file.shareby.controller;

import com.file.shareby.domain.FileData;
import com.file.shareby.domain.SharedData;
import com.file.shareby.domain.User;
import com.file.shareby.service.SharedbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@RequestMapping("/api")
@RestController
public class SharedbyController {

    @Autowired
    SharedbyService sharedbyService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user){
        return  sharedbyService.registerUser(user);
    }


    @PostMapping("/v1/file/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        FileData fileData = sharedbyService.uploadFile(file);
        if (null != fileData) {
            return new ResponseEntity<>(fileData.getId(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/v1/file/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable ("id") String id ){

        FileData file = sharedbyService.getFile(id);
        if(file!=null) {
            return new ResponseEntity<>(new ByteArrayResource(file.getData()),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/v1/file/share")
    public ResponseEntity<SharedData> shreFile(@RequestBody SharedData sharedData){

        return sharedbyService.shareFile(sharedData);
    }

    @GetMapping("/v1/file")
    public ResponseEntity<?> getFiles(){
       return sharedbyService.getFiles();
    }


}

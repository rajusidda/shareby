package com.file.shareby.controller;

import com.file.shareby.DTO.DownloadDTO;
import com.file.shareby.DTO.FilesDTO;
import com.file.shareby.DTO.SharedDataDTO;
import com.file.shareby.DTO.UploadDataDTO;
import com.file.shareby.customexception.FileNotFoundException;
import com.file.shareby.customexception.FileStorageException;
import com.file.shareby.domain.UploadData;
import com.file.shareby.domain.User;
import com.file.shareby.service.SharedbyService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Api(tags = "Shareby Services")
@RequestMapping("/api")
@Slf4j
@RestController
public class SharedbyController {

    @Autowired
    private SharedbyService sharedbyService;

    @PostMapping("/v1/file/upload")
    public ResponseEntity<UploadDataDTO> uploadFile(@RequestParam("file") MultipartFile file, HttpSession httpSession) {
        UploadDataDTO uploadDataDTO;
        try {
            User user = (User) httpSession.getAttribute("user");
            UploadData uploadData = sharedbyService.uploadFile(file, user);
            uploadDataDTO = new UploadDataDTO();
            uploadDataDTO.setId(uploadData.getId());
        } catch (FileStorageException e) {
            log.info("FileStorageException occurred while saving the file");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.info("Exception occurred while saving the file");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(uploadDataDTO, HttpStatus.OK);
    }

    @GetMapping("/v1/file/{id}")
    public ResponseEntity<DownloadDTO> downloadFile(@ModelAttribute DownloadDTO downloadDto, HttpSession httpSession) {
        try {
            User user = (User) httpSession.getAttribute("user");
            String fileResponse = sharedbyService.downloadFile(downloadDto.getId(), user);
            DownloadDTO file = new DownloadDTO();
            file.setFile(fileResponse);
            return new ResponseEntity<>(file, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            log.info("file is not available to download");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug("User don't have permission for downloading this file");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/v1/file/share")
    public ResponseEntity<SharedDataDTO> shreFile(@RequestBody SharedDataDTO sharedDataDTO, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        try {
            SharedDataDTO shareFile = sharedbyService.shareFile(sharedDataDTO, user);
            return new ResponseEntity<>(shareFile, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            log.info("file is not with you to share with others");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.info("User don't have permission for this file to share with others");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/v1/file")
    public ResponseEntity<FilesDTO> getFiles(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        try {
            FilesDTO files = sharedbyService.getFiles(user);
            return new ResponseEntity<>(files, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Exception occurred while getting the files");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

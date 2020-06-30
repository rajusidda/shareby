package com.file.shareby.payload;

import com.file.shareby.domain.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.ManyToOne;

@Data
public class UploadDataDTO {
    private String id;
    private String fileName;
    private String fileType;
    private String fileBy;
    private User user;
}

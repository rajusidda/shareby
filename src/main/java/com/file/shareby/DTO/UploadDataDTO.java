package com.file.shareby.DTO;

import com.file.shareby.domain.User;
import lombok.Data;

@Data
public class UploadDataDTO {
    private String id;
    private String fileName;
    private String fileType;
    private String fileBy;
    private User user;
}

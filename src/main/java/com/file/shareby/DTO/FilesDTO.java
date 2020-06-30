package com.file.shareby.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FilesDTO {
    private List<UploadDataDTO> uploadData;
    private List<SharedDataDTO> sharedData;
}

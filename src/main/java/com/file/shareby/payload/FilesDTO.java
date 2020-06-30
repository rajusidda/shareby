package com.file.shareby.payload;

import com.file.shareby.domain.SharedData;
import com.file.shareby.domain.UploadData;
import lombok.Data;

import java.util.List;

@Data
public class FilesDTO {

    private List<UploadDataDTO> uploadData;
    private List<SharedDataDTO> sharedData;
}

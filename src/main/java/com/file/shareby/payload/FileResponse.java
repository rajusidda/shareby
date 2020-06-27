package com.file.shareby.payload;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;

@Builder
@Data
public class FileResponse {
    private String fileName;
    private ByteArrayResource byteArrayResource;
    private String fileType;
}

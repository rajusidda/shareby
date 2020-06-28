package com.file.shareby.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private String fileID;
    private String email;
}

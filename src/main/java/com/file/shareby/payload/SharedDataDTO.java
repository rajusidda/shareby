package com.file.shareby.payload;

import com.file.shareby.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class SharedDataDTO {
    private String id;
    private String fromUser;
    private List<User> toUsers;
    private String fileId;
    private String fileBy;
}

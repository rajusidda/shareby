package com.file.shareby.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Data
public class FileData {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String fileName;

    private String fileType;

    private String email;

    @Lob
    private byte[] data;

    public FileData(String filename, String email, String fileType, byte[] bytes) {
        this.fileName = filename;
        this.email = email;
        this.fileType = fileType;
        this.data = bytes;

    }
}
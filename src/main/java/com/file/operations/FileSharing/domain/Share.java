package com.file.operations.FileSharing.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String ownerEmial;
    private String userEmail;
    private String fileId;
}

package com.file.operations.FileSharing.repository;

import com.file.operations.FileSharing.domain.DataFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileStorageRepository extends JpaRepository<DataFile,String>  {
   List<DataFile> findAllByEmail(String email);
}

package com.file.shareby.repository;

import com.file.shareby.domain.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileStorageRepository extends JpaRepository<FileData,String>  {
   List<FileData> findAllByEmail(String email);
}

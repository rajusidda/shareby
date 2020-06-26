package com.file.shareby.repository;

import com.file.shareby.domain.DataFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileStorageRepository extends JpaRepository<DataFile,String>  {
   List<DataFile> findAllByEmail(String email);
}

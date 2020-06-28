package com.file.shareby.repository;

import com.file.shareby.domain.UploadData;
import com.file.shareby.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileStorageRepository extends JpaRepository<UploadData,String>  {
   List<UploadData> findByUser(User user);
}

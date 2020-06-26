package com.file.operations.FileSharing.repository;

import com.file.operations.FileSharing.domain.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileSharingRepository extends JpaRepository<Share,Integer>{
    List<Share> findAllByUserEmail(String email);
    Optional<Share> findByFileId(String id);
}

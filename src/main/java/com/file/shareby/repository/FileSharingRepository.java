package com.file.shareby.repository;

import com.file.shareby.domain.SharedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileSharingRepository extends JpaRepository<SharedData,Integer>{
    List<SharedData> findAllByUserEmail(String email);
    Optional<SharedData> findByFileId(String id);
}

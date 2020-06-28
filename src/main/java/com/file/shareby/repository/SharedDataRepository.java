package com.file.shareby.repository;

import com.file.shareby.domain.SharedData;
import com.file.shareby.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharedDataRepository extends JpaRepository<SharedData,String>{
    Optional<SharedData> findByFileId(String id);
    List<SharedData> findByToUsers(User user);
}

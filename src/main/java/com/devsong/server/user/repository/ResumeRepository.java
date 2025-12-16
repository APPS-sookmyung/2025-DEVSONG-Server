package com.devsong.server.user.repository;

import com.devsong.server.user.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    //이력서 조회
    Optional<Resume> findByUserId(Long userId);

}



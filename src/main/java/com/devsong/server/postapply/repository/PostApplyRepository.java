package com.devsong.server.postapply.repository;

import com.devsong.server.postapply.entity.PostApply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostApplyRepository extends JpaRepository<PostApply, Long> {
    PostApply findByPostId(Long postId);
}

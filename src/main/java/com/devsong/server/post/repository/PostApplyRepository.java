package com.devsong.server.post.repository;

import com.devsong.server.post.entity.Post;
import com.devsong.server.post.entity.PostApply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostApplyRepository extends JpaRepository<PostApply, Long> { ;
}

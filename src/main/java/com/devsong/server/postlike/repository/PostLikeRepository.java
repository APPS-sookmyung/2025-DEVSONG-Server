package com.devsong.server.postlike.repository;

import com.devsong.server.post.entity.PostLike;
import com.devsong.server.postlike.entity.PostLike1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike1, Long>  {
    PostLike1 findByPostId(Long postId);
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    void deleteByUserIdAndPostId(Long userId, Long postId);
}

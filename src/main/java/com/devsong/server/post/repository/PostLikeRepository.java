package com.devsong.server.post.repository;

import com.devsong.server.post.entity.Post;
import com.devsong.server.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long>  {
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    void deleteByUserIdAndPostId(Long userId, Long postId);
    Long countByPostId(Long postId);
    List<PostLike> findByUserId(Long userId); //내가 좋아요한 글 조회
}

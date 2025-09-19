package com.devsong.server.post.repository;

import com.devsong.server.post.entity.Post;
import com.devsong.server.post.entity.PostApply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostApplyRepository extends JpaRepository<PostApply, Long> {
    Long countByPost(Post post);

    //지원자 목록 불러오기
    List<PostApply> findByPostId(Long postId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    List<PostApply> findByUserId(Long userId); //내가 지원한 글 조회
}

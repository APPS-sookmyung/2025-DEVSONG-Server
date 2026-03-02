package com.devsong.server.post.repository;

import com.devsong.server.post.entity.Category;
import com.devsong.server.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 전체 조회 - 최신순
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 카테고리별 조회 - 최신순
    Page<Post> findAllByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable);

    // 전체 조회 - 좋아요순
    Page<Post> findAllByOrderByLikeCountDesc(Pageable pageable);

    // 카테고리별 조회 - 좋아요순
    Page<Post> findAllByCategoryOrderByLikeCountDesc(Category category, Pageable pageable);

    // 내가 쓴 글 조회
    List<Post> findByUserIdOrderByIdDesc(Long userId);

    // 모집여부별 조회 - 최신순
    Page<Post> findAllByCategoryInAndClosedOrderByCreatedAtDesc(
            List<Category> categories, boolean closed, Pageable pageable);

    // 모집여부별 조회 - 좋아요순
    Page<Post> findAllByCategoryInAndClosedOrderByLikeCountDesc(
            List<Category> categories, boolean closed, Pageable pageable);

    // 카테고리+모집여부별 조회 - 최신순
    Page<Post> findAllByCategoryAndClosedOrderByCreatedAtDesc(Category category, boolean closed, Pageable pageable);

    // 카테고리+모집여부별 조회 - 좋아요순
    Page<Post> findAllByCategoryAndClosedOrderByLikeCountDesc(Category category, boolean closed, Pageable pageable);

}

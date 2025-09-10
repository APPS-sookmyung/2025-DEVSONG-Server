package com.devsong.server.post.repository;

import com.devsong.server.post.entity.Category;
import com.devsong.server.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    //기본 최신순 정렬
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable); //전체 조회
    Page<Post> findAllByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable); //카테고리별 조회

    @Query("SELECT p FROM Post p LEFT JOIN PostLike pl ON p.id = pl.post.id " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pl.id) DESC")
    List<Post> findByLikeCountDesc(Pageable pageable); //좋아요 상위 9개 게시글 조회

    //전체조회 - 좋아요순
    @Query("SELECT p FROM Post p LEFT JOIN p.postLikes pl " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pl.id) DESC, p.createdAt DESC")
    Page<Post> findAllOrderByLikeCount(Pageable pageable);

    //카테고리별 조회 - 좋아요순
    @Query("SELECT p FROM Post p LEFT JOIN p.postLikes pl " +
            "WHERE p.category = :category " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pl.id) DESC, p.createdAt DESC")
    Page<Post> findAllByCategoryOrderByLikeCount(Category category, Pageable pageable);
}

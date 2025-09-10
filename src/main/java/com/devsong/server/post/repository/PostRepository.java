package com.devsong.server.post.repository;

import com.devsong.server.post.entity.Category;
import com.devsong.server.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable); //전체 조회
    Page<Post> findAllByCategory(Category category, Pageable pageable); //카테고리별 조회
    List<Post> findAllByOrderByIdDesc(); //전체조회
    List<Post> findAllByCategoryOrderByIdDesc(Category category); //카테고리별 조회
    @Query("SELECT p FROM Post p LEFT JOIN PostLike pl ON p.id = pl.post.id " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pl.id) DESC")
    List<Post> findByLikeCountDesc(Pageable pageable); //좋아요 상위 9개 게시글 조회
}

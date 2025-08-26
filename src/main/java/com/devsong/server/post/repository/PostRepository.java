package com.devsong.server.post.repository;

import com.devsong.server.post.entity.Category;
import com.devsong.server.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByIdDesc(); //전체조회
    List<Post> findAllByCategoryOrderByIdDesc(Category category); //카테고리별 조회
}

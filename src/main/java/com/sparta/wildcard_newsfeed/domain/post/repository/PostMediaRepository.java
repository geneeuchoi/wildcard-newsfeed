package com.sparta.wildcard_newsfeed.domain.post.repository;

import com.sparta.wildcard_newsfeed.domain.post.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {
    List<PostMedia> findByPostId(Long postId);
}
package com.sparta.wildcard_newsfeed.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private List<String> s3Urls;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private Long likeCount;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getName();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.likeCount = post.getLikeCount();
    }

    public PostResponseDto(Post post, List<String> s3Urls) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getName();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.s3Urls = s3Urls;
        this.likeCount = post.getLikeCount();
    }
}
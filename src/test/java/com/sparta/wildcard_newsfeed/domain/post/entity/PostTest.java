package com.sparta.wildcard_newsfeed.domain.post.entity;

import com.sparta.wildcard_newsfeed.domain.comment.entity.Comment;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    @DisplayName("게시물 생성 테스트")
    void test1() {
        // Given
        User user = new User("testUser", "password", "test@example.com");
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("Test Title");
        postRequestDto.setContent("Test Content");

        // When
        Post post = new Post(postRequestDto, user);

        // Then
        assertNotNull(post);
        assertEquals("Test Title", post.getTitle());
        assertEquals("Test Content", post.getContent());
        assertEquals(0, post.getLikeCount());
        assertEquals(user, post.getUser());
    }

    @Test
    @DisplayName("게시물 업데이트 테스트")
    void test2() {
        // Given
        User user = new User("testUser", "password", "test@example.com");
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("Original Title");
        postRequestDto.setContent("Original Content");
        Post post = new Post(postRequestDto, user);

        PostRequestDto updateRequestDto = new PostRequestDto();
        updateRequestDto.setTitle("Updated Title");
        updateRequestDto.setContent("Updated Content");

        // When
        post.update(updateRequestDto);

        // Then
        assertEquals("Updated Title", post.getTitle());
        assertEquals("Updated Content", post.getContent());
    }


    @Test
    @DisplayName("게시물에 미디어 추가 테스트")
    void test3() {
        // Given
        User user = new User("testUser", "password", "test@example.com");
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("Test Title");
        postRequestDto.setContent("Test Content");
        Post post = new Post(postRequestDto, user);

        PostMedia postMedia = new PostMedia();
        postMedia.setUrl("IWANNASLEEP.jpg");
        postMedia.setPost(post);
        post.getPostMedias().add(postMedia);

        // When
        List<PostMedia> postMedias = post.getPostMedias();

        // Then
        assertEquals(1, postMedias.size());
        assertEquals("IWANNASLEEP.jpg", postMedias.get(0).getUrl());
    }
}
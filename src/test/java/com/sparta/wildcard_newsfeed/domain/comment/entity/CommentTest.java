package com.sparta.wildcard_newsfeed.domain.comment.entity;

import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    @DisplayName("댓글 생성 테스트")
    void test1() {
        // Given
        User user = new User("testUser", "password", "test@example.com");
        Post post = new Post();
        String content = "이걸 보신다면 당근을 흔들어주세요";

        // When
        Comment comment = new Comment(content, user, post);

        // Then
        assertNotNull(comment);
        assertEquals(content, comment.getContent());
        assertEquals(user, comment.getUser());
        assertEquals(post, comment.getPost());
        assertEquals(0L, comment.getLikeCount());
    }

    @Test
    @DisplayName("댓글 업데이트 테스트")
    void test2() {
        // Given
        User user = new User("testUser", "password", "test@example.com");
        Post post = new Post();
        Comment comment = new Comment("Original Comment", user, post);
        String updatedContent = "Updated Comment";

        // When
        comment.update(updatedContent);

        // Then
        assertEquals(updatedContent, comment.getContent());
    }

}
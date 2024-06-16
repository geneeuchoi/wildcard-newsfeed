package com.sparta.wildcard_newsfeed.domain.comment.service;

import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentRequestDto;
import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentResponseDto;
import com.sparta.wildcard_newsfeed.domain.comment.entity.Comment;
import com.sparta.wildcard_newsfeed.domain.comment.repository.CommentRepository;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostRepository;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "password", "test@example.com");
        post = new Post();
        post.setId(1L);
    }

    @Test
    @DisplayName("댓글 추가 테스트")
    void test1() {
        // Given
        long postId = 1L;
        CommentRequestDto request = new CommentRequestDto();
        request.setContent("Test comment");
        AuthenticationUser authUser = new AuthenticationUser("testUser", "ROLE_USER");
        when(userRepository.findByUsercode(authUser.getUsername())).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Comment comment = new Comment(request.getContent(), user, post);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // When
        CommentResponseDto responseDto = commentService.addComment(postId, request, authUser);

        // Then
        assertNotNull(responseDto);
        assertEquals(request.getContent(), responseDto.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 업데이트 테스트")
    void test2() {
        // Given
        long postId = 1L;
        long commentId = 1L;
        CommentRequestDto request = new CommentRequestDto();
        request.setContent("Updated comment");
        AuthenticationUser authUser = new AuthenticationUser("testUser", "ROLE_USER");
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Comment comment = new Comment("Original comment", user, post);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // When
        CommentResponseDto responseDto = commentService.updateComment(postId, commentId, request, authUser);

        // Then
        assertNotNull(responseDto);
        assertEquals(request.getContent(), responseDto.getContent());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void test3() {
        // Given
        long postId = 1L;
        long commentId = 1L;
        String username = "testUser";
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Comment comment = new Comment("Test comment", user, post);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // When
        commentService.deleteComment(postId, commentId, username);

        // Then
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    @DisplayName("댓글 단일 조회 테스트")
    void test4() {
        // Given
        long postId = 1L;
        Comment comment = new Comment("Test comment", user, post);
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        when(commentRepository.findByPostId(postId)).thenReturn(commentList);

        // When
        List<CommentResponseDto> responseDtos = commentService.findAllCommentsByPostId(postId);

        // Then
        assertNotNull(responseDtos);
        assertEquals(1, responseDtos.size());
        assertEquals(comment.getContent(), responseDtos.get(0).getContent());
    }

    @Test
    @DisplayName("모든 댓글 조회 테스트")
    void test5() {
        // Given
        Comment comment = new Comment("Test comment", user, post);
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        when(commentRepository.findAll()).thenReturn(commentList);

        // When
        List<CommentResponseDto> responseDtos = commentService.findAll();

        // Then
        assertNotNull(responseDtos);
        assertEquals(1, responseDtos.size());
        assertEquals(comment.getContent(), responseDtos.get(0).getContent());
    }
}

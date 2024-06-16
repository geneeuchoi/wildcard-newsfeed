package com.sparta.wildcard_newsfeed.domain.post.service;

import com.sparta.wildcard_newsfeed.domain.file.service.FileService;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostRequestDto;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostResponseDto;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.post.entity.PostMedia;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostMediaRepository;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostRepository;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
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
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostMediaRepository postMediaRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시물 추가 테스트")
    void test1() {
        // Given
        PostRequestDto postRequestDto = mock(PostRequestDto.class);
        AuthenticationUser user = new AuthenticationUser("testUser", "ROLE_USER");
        User foundUser = new User("testUser", "password", "test@example.com");
        when(userRepository.findByUsercode(user.getUsername())).thenReturn(Optional.of(foundUser));
        Post post = new Post(postRequestDto, foundUser);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postRequestDto.getFiles()).thenReturn(new ArrayList<>());

        // When
        PostResponseDto responseDto = postService.addPost(postRequestDto, user);

        // Then
        assertNotNull(responseDto);
        assertEquals(post.getTitle(), responseDto.getTitle());
        assertEquals(post.getContent(), responseDto.getContent());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(postMediaRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("게시물 조회 테스트")
    void test2() {
        // Given
        long postId = 1L;
        Post post = mock(Post.class);
        User user = new User("testUser", "password", "test@example.com");
        List<PostMedia> postMediaList = new ArrayList<>();
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(post.getUser()).thenReturn(user);  // Set the User object
        when(post.getTitle()).thenReturn("Test Title");
        when(post.getContent()).thenReturn("Test Content");
        when(postMediaRepository.findByPostId(postId)).thenReturn(postMediaList);

        // When
        PostResponseDto responseDto = postService.findById(postId);

        // Then
        assertNotNull(responseDto);
        assertEquals(post.getTitle(), responseDto.getTitle());
        assertEquals(post.getContent(), responseDto.getContent());
        verify(postRepository, times(1)).findById(postId);
        verify(postMediaRepository, times(1)).findByPostId(postId);
    }

    @Test
    @DisplayName("게시물 업데이트 테스트")
    void test3() {
        // Given
        long postId = 1L;
        PostRequestDto postRequestDto = mock(PostRequestDto.class);
        AuthenticationUser user = new AuthenticationUser("testUser", "ROLE_USER");
        User foundUser = new User("testUser", "password", "test@example.com");
        Post post = new Post(postRequestDto, foundUser);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postRequestDto.getFiles()).thenReturn(new ArrayList<>());

        // When
        PostResponseDto responseDto = postService.updatePost(postRequestDto, postId, user);

        // Then
        assertNotNull(responseDto);
        assertEquals(post.getTitle(), responseDto.getTitle());
        assertEquals(post.getContent(), responseDto.getContent());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    @DisplayName("게시물 삭제 테스트")
    void test4() {
        // Given
        long postId = 1L;
        AuthenticationUser user = new AuthenticationUser("testUser", "ROLE_USER");
        User foundUser = new User("testUser", "password", "test@example.com");
        Post post = new Post(new PostRequestDto(), foundUser);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // When
        postService.deletePost(postId, user);

        // Then
        verify(postRepository, times(1)).delete(post);
    }
}

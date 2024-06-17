package com.sparta.wildcard_newsfeed.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostPageRequestDto;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostPageResponseDto;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostRequestDto;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostResponseDto;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.post.service.PostService;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.entity.UserRoleEnum;
import com.sparta.wildcard_newsfeed.domain.user.entity.UserStatusEnum;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private User createUser() {
        return User.builder()
                .usercode("testUser")
                .password("password")
                .name("Test User")
                .email("test@example.com")
                .introduce("Hello, I am a test user.")
                .userStatus(UserStatusEnum.ENABLED)
                .authUserAt(LocalDateTime.now())
                .userRoleEnum(UserRoleEnum.USER)
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("게시물 등록 성공")
    void test1() throws Exception {
        // given
        PostRequestDto requestDto = new PostRequestDto("test title", "test content", new ArrayList<>());
        User user = createUser();
        Post post = new Post(requestDto, user);
        PostResponseDto responseDto = new PostResponseDto(post);

        Mockito.when(postService.addPost(any(PostRequestDto.class), any(AuthenticationUser.class)))
                .thenReturn(responseDto);

        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "test content".getBytes());

        // when, then
        mockMvc.perform(multipart("/api/v1/post")
                        .file(file)
                        .param("title", requestDto.getTitle())
                        .param("content", requestDto.getContent()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시물 등록 성공"));
    }

    @Test
    @WithMockUser
    @DisplayName("게시물 전체 조회 성공")
    void test2() throws Exception {
        // given
        List<PostResponseDto> responseDtos = new ArrayList<>();
        Mockito.when(postService.findAll()).thenReturn(responseDtos);

        // when, then
        mockMvc.perform(get("/api/v1/post"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시물 전체 조회 성공"));
    }

    @Test
    @WithMockUser
    @DisplayName("게시물 단일 조회 성공")
    void test3() throws Exception {
        // given
        User user = createUser();
        Post post = new Post(new PostRequestDto("test title", "test content", new ArrayList<>()), user);
        PostResponseDto postResponseDto = new PostResponseDto(post);

        Mockito.when(postService.findById(anyLong())).thenReturn(postResponseDto);

        // when, then
        mockMvc.perform(get("/api/v1/post/{postId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시물 단일 조회 성공"));
    }

    @Test
    @WithMockUser
    @DisplayName("게시물 수정 성공")
    void test4() throws Exception {
        // given
        PostRequestDto requestDto = new PostRequestDto("test title", "test content", new ArrayList<>());
        User user = createUser();
        Post post = new Post(requestDto, user);
        PostResponseDto responseDto = new PostResponseDto(post);

        Mockito.when(postService.updatePost(any(PostRequestDto.class), anyLong(), any(AuthenticationUser.class)))
                .thenReturn(responseDto);

        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "test content".getBytes());

        // when, then
        mockMvc.perform(multipart("/api/v1/post/{postId}", 1L)
                        .file(file)
                        .param("title", requestDto.getTitle())
                        .param("content", requestDto.getContent()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시물 수정 성공"));
    }

    @Test
    @WithMockUser
    @DisplayName("게시물 삭제 성공")
    void test5() throws Exception {
        // given
        Mockito.doNothing().when(postService).deletePost(anyLong(), any(AuthenticationUser.class));

        // when, then
        mockMvc.perform(delete("/api/v1/post/{postId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시물 삭제 성공"));
    }

    @Test
    @WithMockUser
    @DisplayName("페이지네이션 성공")
    void test6() throws Exception {
        // given
        PostPageRequestDto requestDto = new PostPageRequestDto();
        Page<PostPageResponseDto> page = new PageImpl<>(new ArrayList<>());
        Mockito.when(postService.getPostPage(any(PostPageRequestDto.class))).thenReturn(page);

        // when, then
        mockMvc.perform(post("/api/v1/post/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시물 페이지 조회 성공"));
    }

    @Test
    @WithMockUser
    @DisplayName("게시물 등록 실패 - 유효성 검사 실패")
    void test7() throws Exception {
        // when, then
        mockMvc.perform(multipart("/api/v1/post")
                        .param("title", "")
                        .param("content", "test content"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("게시물 단일 조회 실패 - 게시물 없음")
    void test8() throws Exception {
        // given
        Mockito.when(postService.findById(anyLong())).thenThrow(new RuntimeException("게시물을 찾을 수 없습니다."));

        // when, then
        mockMvc.perform(get("/api/v1/post/{postId}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("게시물을 찾을 수 없습니다."));
    }
}

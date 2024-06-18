package com.sparta.wildcard_newsfeed.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.wildcard_newsfeed.config.WebSecurityConfig;
import com.sparta.wildcard_newsfeed.domain.comment.controller.CommentController;
import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentRequestDto;
import com.sparta.wildcard_newsfeed.domain.comment.repository.CommentRepository;
import com.sparta.wildcard_newsfeed.domain.comment.service.CommentService;
import com.sparta.wildcard_newsfeed.domain.post.controller.PostController;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostRepository;
import com.sparta.wildcard_newsfeed.domain.post.service.PostService;
import com.sparta.wildcard_newsfeed.domain.user.controller.UserController;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        })
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @WithMockUser
    @Order(1)
    @DisplayName("댓글 등록 성공")
    void test1() throws Exception {
        //given
        CommentRequestDto requestDto = new CommentRequestDto("댓글 등록");

        String postInfo = objectMapper.writeValueAsString(requestDto);

        //when - then
        mockMvc.perform(post("/api/v1/post/1/comment")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @Order(2)
    @DisplayName("댓글 수정 성공")
    void test2() throws Exception {
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정 테스트");

        String postInfo = objectMapper.writeValueAsString(requestDto);

        //when - then
        mockMvc.perform(put("/api/v1/post/1/comment/1")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @Order(3)
    @DisplayName("댓글 삭제 성공")
    void test3() throws Exception {
        // given
        Mockito.doNothing().when(commentService).deleteComment(anyLong(), anyLong(), any(String.class));

        // when, then
        mockMvc.perform(delete("/api/v1/post/{postId}/comment/{commentId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("댓글 삭제 성공"));
    }

    @Test
    @WithMockUser
    @Order(4)
    @DisplayName("댓글 등록 실패 - 유효성 검사 실패")
    void test4() throws Exception {
        // given
        CommentRequestDto requestDto = new CommentRequestDto("");

        // when, then
        mockMvc.perform(post("/api/v1/post/{postId}/comment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @Order(5)
    @DisplayName("댓글 수정 실패 - 유효성 검사 실패")
    void test5() throws Exception {
        // given
        CommentRequestDto requestDto = new CommentRequestDto("");

        // when, then
        mockMvc.perform(put("/api/v1/post/{postId}/comment/{commentId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @Order(6)
    @DisplayName("댓글 삭제 실패 - 댓글 없음")
    void test6() throws Exception {
        // given
        Mockito.doThrow(new RuntimeException("댓글을 찾을 수 없습니다.")).when(commentService).deleteComment(anyLong(), anyLong(), any(String.class));

        // when, then
        mockMvc.perform(delete("/api/v1/post/{postId}/comment/{commentId}", 1L, 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("댓글을 찾을 수 없습니다."));
    }
}

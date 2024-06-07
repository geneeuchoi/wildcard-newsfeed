package com.sparta.wildcard_newsfeed.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @Schema(description = "게시물 제목", example = "제목")
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @Schema(description = "게시물 내용", example = "내용")
    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;
}
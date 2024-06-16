package com.sparta.wildcard_newsfeed.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용은 필수 입력 값입니다.")
    @Schema(description = "댓글 내용", example = "내용")
    private String content;

    public CommentRequestDto(String content) {
        this.content = content;
    }
}
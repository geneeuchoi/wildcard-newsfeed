package com.sparta.wildcard_newsfeed.domain.liked.dto;

import com.sparta.wildcard_newsfeed.domain.liked.entity.ContentsTypeEnum;
import com.sparta.wildcard_newsfeed.domain.liked.entity.Liked;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LikedResponseDto {

    private Long likeId;
    private Long contentsId;
    private ContentsTypeEnum contentsType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LikedResponseDto(Liked liked) {
        this.likeId = liked.getId();
        this.contentsId = liked.getContentsId();
        this.contentsType = liked.getContentsType();
        this.createdAt = liked.getCreatedAt();
        this.updatedAt = liked.getUpdatedAt();
    }
}
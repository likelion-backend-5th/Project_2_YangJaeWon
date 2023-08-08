package com.example.project2.dto;

import com.example.project2.entity.Article;
import com.example.project2.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleDto {
    private Long id;
    private User user;
    private String content;

    public static ArticleDto fromEntity(Article entity) {
        return ArticleDto.builder()
                .id(entity.getId())
                .user(entity.getUser())
                .content(entity.getContent())
                .build();
    }
}

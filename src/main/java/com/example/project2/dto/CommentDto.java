package com.example.project2.dto;

import com.example.project2.entity.Article;
import com.example.project2.entity.Comment;
import com.example.project2.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    private Long id;
    private Article article;
    private User user;
    private String content;

    public static CommentDto fromEntity(Comment entity) {
        return CommentDto.builder()
                .id(entity.getId())
                .article(entity.getArticle())
                .user(entity.getUser())
                .content(entity.getContent())
                .build();
    }
}

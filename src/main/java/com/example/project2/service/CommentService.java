package com.example.project2.service;

import com.example.project2.dto.CommentDto;
import com.example.project2.entity.Article;
import com.example.project2.entity.Comment;
import com.example.project2.entity.User;
import com.example.project2.repository.ArticleRepository;
import com.example.project2.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final List<CommentDto> commentList = new ArrayList<>();

    public Comment createComment(CommentDto commentDto, User loginUser) {
        Article article = articleRepository.findById(commentDto.getArticle().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 피드를 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .id(commentDto.getId())
                .content(commentDto.getContent())
                .article(commentDto.getArticle())
                .user(commentDto.getUser())
                .build();


        commentList.add(commentDto);

        return commentRepository.save(comment);

    }

    public List<CommentDto> readAllComments(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        List<CommentDto> commentList = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDto commentDto = CommentDto.fromEntity(comment);
            commentList.add(commentDto);
        }

        return commentList;
    }
}

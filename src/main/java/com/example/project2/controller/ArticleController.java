package com.example.project2.controller;

import com.example.project2.dto.ArticleDto;
import com.example.project2.dto.CommentDto;
import com.example.project2.entity.ArticleImages;
import com.example.project2.entity.CustomUserDetails;
import com.example.project2.entity.User;
import com.example.project2.service.ArticleService;
import com.example.project2.service.CommentService;
import com.example.project2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final UserService userService;
    private final ArticleService articleService;
    private final CommentService commentService;

    @GetMapping("/articles/create-article") //게시글 생성 페이지
    public String createView() {
        return "createArticle";
    }

    @PostMapping("/articles/create")
    public String createArticle(
            ArticleDto articleDto,
            @RequestParam("file")MultipartFile file,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User loginedUser = userService.readUser(userDetails.getUsername());

        articleService.createArticle(articleDto, file, loginedUser);
        return "redirect:/articles";

    }

    //페이징처리
    @GetMapping("/articles")
    public String article(
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model
    ) {
        Page<ArticleDto> articles = articleService.readArticleAllPaged(page);

        model.addAttribute("articleList", articles.getContent());
        model.addAttribute("currentPage", articles.getNumber() + 1);
        model.addAttribute("totalPages", articles.getTotalPages());

        return "articles";
    }

    //피드 하나 읽기
    @GetMapping("/articles/{id}")
    public String readOne(
            @PathVariable("id") Long articleId,
            Model model,
            Authentication authentication
    ) {
        ArticleDto articleDto = articleService.readArticle(articleId);
        if(articleDto != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            model.addAttribute("article", articleDto);
            model.addAttribute("sessionDto", userDetails);
            model.addAttribute("commentList", commentService.readAllComments(articleId));
            return "readArticle";
        } else {
            return "redirect:/articles";
        }
    }

    @PostMapping("/articles/{id}")
    public String createComment(@PathVariable("id") Long articleId,
                               @RequestParam("content") String content,
                               Authentication authentication,
                               Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User loginedUser = userService.readUser(userDetails.getUsername());
        CommentDto commentDto = CommentDto.builder()
                .user(loginedUser)
                .content(content)
                //.article()
                .build();
        commentService.createComment(commentDto, loginedUser);

        return "redirect:/articles/" + articleId;
    }

}

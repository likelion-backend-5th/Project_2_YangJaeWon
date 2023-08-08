package com.example.project2.service;

import com.example.project2.dto.ArticleDto;
import com.example.project2.entity.Article;
import com.example.project2.entity.ArticleImages;
import com.example.project2.entity.User;
import com.example.project2.repository.ArticleImagesRepository;
import com.example.project2.repository.ArticleRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleImagesRepository articleImagesRepository;
    private static final int PAGE_SIZE = 5;
    public Article createArticle(ArticleDto articleDto, MultipartFile file, User loginUser) {
        Article article = Article.builder()
                .user(loginUser)
                .content(articleDto.getContent())
                .build();

        if (file != null && !file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            String uploadFolder = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
            try {

                java.io.File saveFile = new java.io.File(uploadFolder+"\\"+fileName + fileExtension);
                file.transferTo(saveFile);
                // ArticleImages 엔티티에 파일 정보 저장
                ArticleImages articleImage = new ArticleImages();
                articleImage.setImageUrl(uploadFolder + fileName);
                articleImage.setArticle(article);
                articleImagesRepository.save(articleImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        articleRepository.save(article);
        return article;
    }
    public Page<ArticleDto> readArticleAllPaged(int page) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").descending());
        Page<Article> articles = articleRepository.findAll(pageable);
        return articles.map(ArticleDto::fromEntity);
    }

    public ArticleDto readArticle(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if(optionalArticle.isPresent()) {
            Article article = optionalArticle.get();

            return ArticleDto.fromEntity(article);
        }
        return null;
    }

    public void updateArticle(Long articleId, ArticleDto articleDto, User loginUser) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if(optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            if(article.getUser().getUsername().equals(loginUser.getUsername())) {
                article.setTitle(articleDto.getTitle());
                article.setContent(article.getContent());
                articleRepository.save(article);
            } else throw new IllegalArgumentException("수정 권한이 없습니다.");
        } throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다: " + articleId);
    }

    public void deleteArticle(Long articleId, User loginUser) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if(optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            if(article.getUser().getUsername().equals(loginUser.getUsername())) {
                articleRepository.deleteById(articleId);
            } else throw new IllegalArgumentException("삭제 권한이 없습니다.");
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


}

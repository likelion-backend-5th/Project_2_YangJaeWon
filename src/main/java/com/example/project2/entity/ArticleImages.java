package com.example.project2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ArticleImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "FK_ARTICLEIMAGES_ARTICLE"))
    private Article article;

    private String imageUrl;

}

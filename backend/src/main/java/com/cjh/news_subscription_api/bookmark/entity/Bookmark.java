package com.cjh.news_subscription_api.bookmark.entity;

import com.cjh.news_subscription_api.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(
        name = "bookmark",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","url"})
)
public class Bookmark {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String title; //뉴스 제목
    private String url; //뉴스URL
    private String description; //뉴스 요약
    private String pubDate; //발행일
    private String memo; // 사용자 메모

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    public Bookmark(User user,String title, String url, String description, String pubDate) {
        this.user = user;
        this.title = title;
        this.url = url;
        this.description = description;
        this.pubDate = pubDate;
    }
}

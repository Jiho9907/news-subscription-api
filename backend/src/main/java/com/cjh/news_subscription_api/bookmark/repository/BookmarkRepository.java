package com.cjh.news_subscription_api.bookmark.repository;

import com.cjh.news_subscription_api.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUserId(Long userId);
    boolean existsByUserIdAndUrl(Long userId, String url);
    void deleteByUserIdAndUrl(Long userId, String url);
}

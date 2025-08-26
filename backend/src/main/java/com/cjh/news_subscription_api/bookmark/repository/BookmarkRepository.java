package com.cjh.news_subscription_api.bookmark.repository;

import com.cjh.news_subscription_api.bookmark.entity.Bookmark;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUserId(Long userId);
    boolean existsByUserIdAndUrl(Long userId, String url);

    @Modifying
    @Transactional
    @Query("delete from Bookmark b where b.user.id = :userId and b.url = :url")
    int deleteByUserIdAndUrl(@Param("userId") Long userId, @Param("url") String url);
}

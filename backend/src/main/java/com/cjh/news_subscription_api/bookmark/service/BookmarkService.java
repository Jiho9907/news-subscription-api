package com.cjh.news_subscription_api.bookmark.service;

import com.cjh.news_subscription_api.bookmark.entity.Bookmark;
import com.cjh.news_subscription_api.bookmark.repository.BookmarkRepository;
import com.cjh.news_subscription_api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    // 이미 찜한 뉴스면 삭제하고 아니면 저장(toggle)
    public void toggleBookmark(User user, String title, String url, String description, String pubDate) {
        if(bookmarkRepository.existsByUserIdAndUrl(user.getId(), url)) {
            bookmarkRepository.deleteByUserIdAndUrl(user.getId(), url);
        } else {
            Bookmark bookmark = new Bookmark(user, title, url, description, pubDate);
            bookmarkRepository.save(bookmark);
        }
    }

    // 해당 유저의 모든 찜한 뉴스 가져오기
    public List<Bookmark> getUserBookmarks(Long userId) {
        return bookmarkRepository.findByUserId(userId);
    }
}

package com.cjh.news_subscription_api.bookmark.service;

import com.cjh.news_subscription_api.bookmark.entity.Bookmark;
import com.cjh.news_subscription_api.bookmark.repository.BookmarkRepository;
import com.cjh.news_subscription_api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    // 이미 찜한 뉴스면 삭제하고 아니면 저장(toggle)
    public void toggleBookmark(User user, String title, String url, String description, String pubDate) {
        if(bookmarkRepository.existsByUserIdAndUrl(user.getId(), url)) {

            long deletedCount = bookmarkRepository.deleteByUserIdAndUrl(user.getId(), url);
            if (deletedCount == 0) {
                throw new IllegalStateException("삭제할 찜이 없습니다.");
            }
        } else {
            Bookmark bookmark = new Bookmark(user, title, url, description, pubDate);
            bookmarkRepository.save(bookmark);
        }
    }

    // 해당 유저의 모든 찜한 뉴스 가져오기
    public List<Bookmark> getUserBookmarks(Long userId) {
        return bookmarkRepository.findByUserId(userId);
    }

    // 메모 에러방지 로직
    public void updateMemo(Long userId, Long bookmarkId, String memo) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 찜입니다."));

        if (!bookmark.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("해당 찜에 대한 수정 권한이 없습니다.");
        }
        // 유효성 검사 (예: 너무 긴 메모 제한)
        if (memo != null && memo.length() > 500) {
            throw new IllegalArgumentException("메모는 최대 500자까지 작성할 수 있습니다.");
        }

        bookmark.setMemo(memo);
        bookmarkRepository.save(bookmark);
    }
}

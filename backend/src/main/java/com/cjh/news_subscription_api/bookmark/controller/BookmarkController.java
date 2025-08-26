package com.cjh.news_subscription_api.bookmark.controller;

import com.cjh.news_subscription_api.bookmark.dto.BookmarkRequestDto;
import com.cjh.news_subscription_api.bookmark.entity.Bookmark;
import com.cjh.news_subscription_api.bookmark.service.BookmarkService;
import com.cjh.news_subscription_api.common.response.ApiResponse;
import com.cjh.news_subscription_api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    // 뉴스 찜 추가 또는 제거
    @PostMapping("/toggle")
    public ApiResponse<?> toggleBookmark(@AuthenticationPrincipal User user,
                                         @RequestBody BookmarkRequestDto dto) {
        bookmarkService.toggleBookmark(user, dto.getTitle(), dto.getUrl(), dto.getDescription(), dto.getPubDate());
        return ApiResponse.success("찜 상태가 변경되었습니다.");
    }

    // 내가 찜한 뉴스 목록 조회
    @GetMapping("/urls")
    public ApiResponse<List<String>> getBookmarkUrls(@AuthenticationPrincipal User user) {
        List<String> urls = bookmarkService.getUserBookmarks(user.getId())
                .stream()
                .map(Bookmark::getUrl)
                .toList();
        return ApiResponse.success(urls);
    }
}

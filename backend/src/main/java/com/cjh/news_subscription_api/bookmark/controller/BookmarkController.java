package com.cjh.news_subscription_api.bookmark.controller;

import com.cjh.news_subscription_api.bookmark.dto.BookmarkRequestDto;
import com.cjh.news_subscription_api.bookmark.dto.BookmarkResponseDto;
import com.cjh.news_subscription_api.bookmark.entity.Bookmark;
import com.cjh.news_subscription_api.bookmark.service.BookmarkService;
import com.cjh.news_subscription_api.common.response.ApiResponse;
import com.cjh.news_subscription_api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Map;

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

    // 찜한 뉴스 url 목록 조회
    @GetMapping("/urls")
    public ApiResponse<List<String>> getBookmarkUrls(@AuthenticationPrincipal User user) {
        List<String> urls = bookmarkService.getUserBookmarks(user.getId())
                .stream()
                .map(Bookmark::getUrl)
                .toList();
        return ApiResponse.success(urls);
    }

    // 찜한 뉴스 목록 조회
    @GetMapping("/list")
    public ApiResponse<List<BookmarkResponseDto>> getUserBookmarks(@AuthenticationPrincipal User user) {
        List<Bookmark> bookmarks = bookmarkService.getUserBookmarks(user.getId());

        List<BookmarkResponseDto> result = bookmarks.stream()
                .map(BookmarkResponseDto::from)
                .toList();

        return ApiResponse.success(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateMemo(@PathVariable Long id,
                                                     @AuthenticationPrincipal User user,
                                                     @RequestBody Map<String, String> req) {
        try {
            bookmarkService.updateMemo(user.getId(), id, req.get("memo"));
            return ResponseEntity.ok(ApiResponse.success("메모가 저장되었습니다."));
        } catch (IllegalArgumentException | AccessDeniedException e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("서버 오류가 발생했습니다."));
        }
    }

}

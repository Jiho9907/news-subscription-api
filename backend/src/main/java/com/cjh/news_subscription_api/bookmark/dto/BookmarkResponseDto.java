package com.cjh.news_subscription_api.bookmark.dto;

import com.cjh.news_subscription_api.bookmark.entity.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookmarkResponseDto {
    private Long id;
    private String title;
    private String url;
    private String description;
    private String pubDate;
    private String memo;

    public static BookmarkResponseDto from(Bookmark b) {
        return new BookmarkResponseDto(
                b.getId(),
                b.getTitle(),
                b.getUrl(),
                b.getDescription(),
                b.getPubDate(),
                b.getMemo()
        );
    }
}

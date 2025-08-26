package com.cjh.news_subscription_api.bookmark.dto;

import lombok.Getter;

@Getter
public class BookmarkRequestDto {
    private String title;
    private String url;
    private String description;
    private String pubDate;
}

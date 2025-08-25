package com.cjh.news_subscription_api.newsapi.service;

import com.cjh.news_subscription_api.config.NaverNewsApiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NewsApiService {

    private final NaverNewsApiConfig naverNewsApiConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Map<String, String>> searchNews(String keyword) {
        String uri = UriComponentsBuilder
                .fromHttpUrl(naverNewsApiConfig.getApiUrl())
                .queryParam("query", keyword)
                .queryParam("display", 9)
                .queryParam("sort","date") //최신순
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", naverNewsApiConfig.getClientId());
        headers.set("X-Naver-Client-secret", naverNewsApiConfig.getClientSecret());

        HttpEntity<String> request = new HttpEntity<>(headers);

        try{
            // 기사 내용 받아오기
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, request, Map.class);
            List<Map<String, String>> items = (List<Map<String, String>>) response.getBody().get("items");
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

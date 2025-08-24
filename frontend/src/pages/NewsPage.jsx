import React, { useEffect, useState } from "react";
import KeywordSelector from "../features/Subscription/KeywordSelector";
import axiosInstance from "../api/axiosInstance";

function NewsPage() {
    const [keywords, setKeywords] = useState([]);
    const [news, setNews] = useState([]);

    useEffect(() => {
        const fetchNews = async () => {
            if (keywords.length === 0) {
                setNews([]); // 키워드 없으면 뉴스도 비움
                return;
            }

            const res = await axiosInstance.post("/api/news/recommend", {
                keywords,
            });

            setNews(res.data.data); // 뉴스 목록 상태 저장
        };

        fetchNews();
    }, [keywords]); // 키워드가 바뀔 때마다 뉴스 요청

    return (
        <div>
            <h1>맞춤 뉴스</h1>

            <KeywordSelector onKeywordChange={setKeywords} />

            {keywords.length === 0 ? (
                <p>관심 키워드를 추가하면 뉴스가 표시됩니다.</p>
            ) : news.length === 0 ? (
                <p>뉴스를 불러오는 중...</p>
            ) : (
                <ul>
                    {news.map((item, idx) => (
                        <li key={idx}>
                            <a href={item.url} target="_blank" rel="noreferrer">
                                {item.title}
                            </a>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default NewsPage;

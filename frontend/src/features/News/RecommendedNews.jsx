import { useEffect, useState } from "react";
import axiosInstance from "../../api/axiosInstance.js";
import NewsList from "../../hooks/NewsList.jsx";
import './RecommendedNews.css';

const RecommendedNews = ({ keywords }) => {
    const [newsMap, setNewsMap] = useState({});
    const [bookmarkedUrls, setBookmarkedUrls] = useState([]);

    useEffect(() => {
        const fetchNews = async () => {
            try {
                const res = await axiosInstance.get("/news/recommend");
                setNewsMap(res.data.data);
            } catch (err) {
                console.error("뉴스 로딩 실패", err);
            }
        };

        const fetchBookmarks = async () => {
            try {
                const res = await axiosInstance.get("/bookmarks/urls");
                setBookmarkedUrls(res.data.data);
            } catch (e) {
                console.error("찜 목록 로딩 실패", e);
            }
        }

        if (keywords && keywords.length > 0) {
            fetchNews();
            fetchBookmarks();
        }
    }, [keywords]);

    const handleBookmark = async (article) => {
        const url = article.link;
        const isBookmarked = bookmarkedUrls.includes(url);

        try {
            await axiosInstance.post("/bookmarks/toggle", {
                title: article.title,
                url,
                description: article.description,
                pubDate: article.pubDate
            });

            setBookmarkedUrls(prev =>
                isBookmarked ? prev.filter(u => u !== url) : [...prev, url]
            );
        } catch (e) {
            console.error("찜 처리 실패", e);
        }
    };

    if (!keywords || keywords.length === 0) {
        return <p>키워드를 먼저 선택해주세요.</p>;
    }

    return (
        <div className="news-grid">
            {Object.entries(newsMap).map(([keyword, articles]) => (
                <div key={keyword} className="news-section">
                    <h3>{keyword}</h3>
                    <NewsList
                        articles={articles}
                        bookmarkedUrls={bookmarkedUrls}
                        handleBookmark={handleBookmark}
                        showSlider={true}
                    />
                </div>
            ))}
        </div>
    );
};

export default RecommendedNews;

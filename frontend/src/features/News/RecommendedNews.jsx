import {useEffect, useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";
import './RecommendedNews.css';

function truncateHTML(htmlString, maxLength) {
    const tempDiv = document.createElement("div");
    tempDiv.innerHTML = htmlString;
    const text = tempDiv.textContent || tempDiv.innerText || "";
    return text.length > maxLength ? text.slice(0, maxLength) + "..." : text;
}

const RecommendedNews = ({ keywords }) => {
    const [newsMap, setNewsMap] = useState({});
    const [slideIndexMap, setSlideIndexMap] = useState({});

    useEffect(() => {
        if (!keywords || keywords.length === 0) {
            setNewsMap({});
            return;
        }

        const fetchNews = async () => {
            try {
                const res = await axiosInstance.get("/news/recommend");
                // 응답 형식이 { keyword: [{title, link, description}, ...], ... } 일 경우
                setNewsMap(res.data.data);

                // 초기 슬라이드 인덱스 0으로 설정
                const initSlide = {};
                Object.keys(res.data.data).forEach(k => initSlide[k] = 0);
                setSlideIndexMap(initSlide);
            } catch (err) {
                console.error("뉴스 로딩 실패", err);
            }
        };

        fetchNews();
    }, [keywords]);

    const handleNextSlide = (keyword, totalLength) => {
        setSlideIndexMap(prev => {
            const current = prev[keyword] || 0;
            const next = current + 3;
            return {
                ...prev,
                [keyword]: next >= totalLength ? current : next
            };
        });
    };

    const handlePrevSlide = (keyword) => {
        setSlideIndexMap(prev => {
            const current = prev[keyword] || 0;
            const prevIndex = current - 3;
            return {
                ...prev,
                [keyword]: prevIndex < 0 ? 0 : prevIndex
            };
        });
    };

    if (!keywords || keywords.length === 0) {
        return <p>키워드를 먼저 선택해주세요.</p>
    }

    return (
        <div className="news-grid">
            {Object.entries(newsMap).map(([keyword, articles]) => {
                const currentIndex = slideIndexMap[keyword] || 0;
                const visibleArticles = articles.slice(currentIndex, currentIndex + 3);

                return (
                    <div key={keyword} className="news-section">
                        <h3>{keyword}</h3>
                        <div className="news-list">
                            <button onClick={() => handlePrevSlide(keyword)}
                                    className="prev-button"
                                    style={{ visibility: currentIndex > 0 ? 'visible' : 'hidden' }}
                            >
                                ◀
                            </button>
                            {visibleArticles.map((article, idx) => (
                                <div key={idx} className="news-card">
                                    <div className="news-content">
                                        <a href={article.link} target="_blank" rel="noreferrer">
                                            <h4 dangerouslySetInnerHTML={{ __html: article.title }} />
                                        </a>
                                        <p dangerouslySetInnerHTML={{ __html: truncateHTML(article.description, 150) }} />
                                        <small className="pub-date">발행일 : {new Date(article.pubDate).toLocaleString()}</small>
                                    </div>
                                </div>
                            ))}
                            <button onClick={() => handleNextSlide(keyword, articles.length)}
                                    className="next-button"
                                    style={{ visibility: currentIndex + 3 < articles.length ? 'visible' : 'hidden' }}
                            >
                                ▶
                            </button>
                        </div>
                    </div>
                );
            })}
        </div>
    );
};
export default RecommendedNews;
import {useEffect, useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";

const RecommendedNews = ({ keywords }) => {
    const [newsMap, setNewsMap] = useState({});

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
            } catch (err) {
                console.error("뉴스 로딩 실패", err);
            }
        };

        fetchNews();
    }, [keywords]);


    if(!keywords || keywords.length === 0) {
        return <p>키워드를 먼저 선택해주세요.</p>
    }

    return (
        <div>
            {Object.entries(newsMap).map(([keyword, articles]) => (
                <div key={keyword} style={{ marginBottom: "20px" }}>
                    <h3>{keyword}</h3>
                    <ul>
                        {articles.map((article, idx) => (
                            <li key={idx}>
                                <a href={article.link} target="_blank" rel="noreferrer">
                                    {article.title}
                                </a>
                                <p>{article.description}</p>
                            </li>
                        ))}
                    </ul>
                </div>
            ))}
        </div>
    );
}
export default RecommendedNews;
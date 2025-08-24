import {useEffect, useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";

const RecommendedNews = ({ keywords }) => {
    const [newsMap, setNewsMap] = useState({});

    useEffect(() => {
        if(!keywords || keywords.length === 0) return;

        const fetchNews = async () => {
            try{
                const res = await axiosInstance.get("/api/news/recommend", {
                   params: {keywords: keywords.join(',')},
                });
                setNewsMap(res.data.data);
            } catch (err) {
                console.error("뉴스 로딩 실패");
            }

            if(!keywords || keywords.length === 0) {
                return <p>키워드를 먼저 선택해주세요.</p>
            }

            return (
                <div>
                    {Object.entries(newsMap).map(([keyword, articles]) => (
                        <div key={keyword}>
                            <h3>{keyword}</h3>
                            <ul>
                                {articles.map((article, i) => (
                                    <li key={i}>
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
            )
        }
    })
}
export default RecommendedNews;
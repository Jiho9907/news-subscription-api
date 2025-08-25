import React, { useEffect, useState } from "react";
import KeywordSelector from "../features/Subscription/KeywordSelector";
import axiosInstance from "../api/axiosInstance";
import RecommendedNews from "../features/News/RecommendedNews.jsx";

function NewsPage() {
    const [keywords, setKeywords] = useState([]);

    return (
        <div>
            <h1>나만의 뉴스</h1>

            {/* 키워드 선택 컴포넌트 */}
            <KeywordSelector onKeywordChange={setKeywords} />

            {/* 선택된 키워드 기반 뉴스 렌더링 */}
            <RecommendedNews keywords={keywords} />
        </div>
    );
}

export default NewsPage;

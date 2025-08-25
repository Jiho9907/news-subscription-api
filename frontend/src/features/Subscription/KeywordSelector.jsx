import {useEffect, useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";
import './KeywordSelector.css';

const OPTIONS = [
    "정치", "경제", "사회", "스포츠", "연예",
    "AI", "테크", "게임", "부동산", "주식",
    "환경", "건강", "음식", "여행", "교육",
    "스타트업", "음악", "날씨", "반도체", "로봇"
];

function KeywordSelector({ onKeywordChange }) {
    const [selectedKeywords, setSelectedKeywords] = useState([]);

    // 처음에 서버에서 기존 저장된 키워드 로드
    useEffect(() => {

        const fetchKeywords = async () => {
            try {
                const res = await axiosInstance.get("/subscriptions/keywords");
                const keywords = res.data.data.map(k => k.keyword);
                setSelectedKeywords(keywords);
                onKeywordChange && onKeywordChange(keywords);
            } catch (e) {
                console.error("키워드 불러오기 실패:", e);
            }
        };
        fetchKeywords();
    }, []);

    // 버튼 클릭 시 토글 함수
    const toggleKeyword = (keyword) => {
        setSelectedKeywords(prev =>
            prev.includes(keyword) ? prev.filter(k => k !== keyword) : [...prev, keyword]
        );
    };

    // 저장 함수: 서버에 선택된 키워드 일괄 저장
    const handleSave = async () => {
        try {
            await axiosInstance.post("/subscriptions", { keywords: selectedKeywords });
            alert("키워드 저장 완료!");
            onKeywordChange && onKeywordChange(selectedKeywords);
        } catch (e) {
            console.error("키워드 저장 실패:", e);
            alert("키워드 저장에 실패했습니다.");
        }
    };

    return (
        <div className="keyword-selector">
            <h4>관심 키워드를 선택하세요</h4>
            <div className="keyword-buttons">
                {OPTIONS.map(keyword => (
                    <button
                        key={keyword}
                        onClick={() => toggleKeyword(keyword)}
                        className={`keyword-button ${selectedKeywords.includes(keyword) ? 'selected' : ''}`}
                        type="button"
                    >
                        {keyword}
                    </button>
                ))}
            </div>
            <button
                className="save-button"
                onClick={handleSave}
                disabled={selectedKeywords.length === 0}>
                저장
            </button>
        </div>
    )
}
export default KeywordSelector;
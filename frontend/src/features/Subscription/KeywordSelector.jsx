import {useEffect, useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";
import Select from 'react-select';

const OPTIONS = [
    "경제",
    "정치",
    "스포츠",
    "AI",
    "테크",
    "연예",
    "건강",
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
        <div>
            <h3>관심 키워드를 선택하세요</h3>
            <div style={{ display: "flex", flexWrap: "wrap", gap: "8px", marginBottom: "12px" }}>
                {OPTIONS.map(keyword => (
                    <button
                        key={keyword}
                        onClick={() => toggleKeyword(keyword)}
                        style={{
                            padding: "6px 12px",
                            borderRadius: "20px",
                            border: selectedKeywords.includes(keyword) ? "2px solid #007bff" : "1px solid #ccc",
                            backgroundColor: selectedKeywords.includes(keyword) ? "#e7f1ff" : "#fff",
                            cursor: "pointer",
                        }}
                        type="button"
                    >
                        {keyword}
                    </button>
                ))}
            </div>
            <button onClick={handleSave} disabled={selectedKeywords.length === 0}>
                저장
            </button>
        </div>
    )
}
export default KeywordSelector;
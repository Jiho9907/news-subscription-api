import {useEffect, useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";
import Select from 'react-select';

const OPTIONS = [
    { label: "경제", value: "경제" },
    { label: "정치", value: "정치" },
    { label: "스포츠", value: "스포츠" },
    { label: "AI", value: "AI" },
    { label: "테크", value: "테크" },
    { label: "연예", value: "연예" },
    { label: "건강", value: "건강" },
];

function KeywordSelector({onKeywordChange}) {
    const [selected, setSelected] = useState([]);


    useEffect(() => {
        const fetchKeywords = async () => {
            try {
                const res = await axiosInstance.get("/api/subscriptions");
                const keywords = res.data.data.map(k => ({
                    label: k.keyword,
                    value: k.keyword
                }));
                setSelected(keywords);
                onKeywordChange && onKeywordChange(keywords.map(k => k.value));
            } catch (e) {
                console.error("키워드 불러오기 실패:", e);
            }
        };
        fetchKeywords();
    }, []);

    const handleSave = async () => {
        try {
            // 기존 키워드 모두 삭제 후 재저장
            await axiosInstance.delete("/api/subscriptions");

            await Promise.all(
                selected.map(k =>
                    axiosInstance.post("/api/subscriptions", { keyword: k.value })
                )
            );

            alert("키워드 저장 완료!");
            onKeywordChange && onKeywordChange(selected.map(k => k.value));
        } catch (e) {
            console.error("키워드 저장 실패:", e);
        }
    };

    return (
        <div>
            <h3>관심 키워드를 선택하세요</h3>
            <Select
                options={OPTIONS}
                value={selected}
                onChange={setSelected}
                labelledBy="Select"
                overrideStrings={{ selectSomeItems: "키워드를 선택하세요" }}
            />
            <button onClick={handleSave}>저장</button>
        </div>
    )
}
export default KeywordSelector;
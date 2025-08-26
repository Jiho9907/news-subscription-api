import bookmark, { useEffect, useState } from "react";
import axiosInstance from "../../api/axiosInstance.js";
import NewsList from "../../hooks/NewsList.jsx";
import './BookmarkList.css';
import '../News/RecommendedNews.css';

function BookmarkList() {
    const [bookmarks, setBookmarks] = useState([]);
    const [bookmarkedUrls, setBookmarkedUrls] = useState([]);
    const [memos, setMemos] = useState({});
    const [editingMemo, setEditingMemo] = useState({});

    useEffect(() => {
        const fetchBookmarks = async () => {
            try {
                const res = await axiosInstance.get(`/bookmarks/list`);
                setBookmarks(res.data.data);

                // memo 상태 초기화
                const initialMemos = {};
                const initialEditing = {};
                res.data.data.forEach(b => {
                    initialMemos[b.id] = b.memo || '';
                    const initialEditing = {};
                });
                setMemos(initialMemos);
                setEditingMemo(initialEditing);

                const urlsRes = await axiosInstance.get(`/bookmarks/urls`);
                setBookmarkedUrls(urlsRes.data.data);
            } catch (e) {
                console.error("찜한 뉴스 목록 로딩 실패", e);
            }
        };

        fetchBookmarks();
    }, []);

    const handleBookmark = async (article) => {
        const url = article.url;
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

            setBookmarks(prev =>
                isBookmarked ? prev.filter(b => b.url !== url) : prev
            );
        } catch (e) {
            console.error("찜 처리 실패", e);
        }
    };

    const handleMemoChange = (id, value) => {
        setMemos(prev => ({...prev, [id]: value}));
    }
    const saveMemo = async (id) => {
        const memo = memos[id];

        // 클라이언트 유효성 검사
        if(memo && memo.length > 500) {
            alert("메모는 최대 500자까지 입력할 수 있습니다.");
            return;
        }

        try {
            await axiosInstance.put(`/bookmarks/${id}`, {
                memo: memo,
            });
            alert("메모 저장 완료");

            // 저장 후 편집 모드 false로 전환
            setEditingMemo(prev => ({ ...prev, [id]: false }));

            // 북마크 목록의 메모도 즉시 반영
            setBookmarks(prev => prev.map(b => b.id === id ? { ...b, memo } : b));
        } catch (e) {
            if(e.response && e.response.data && e.response.data.message) {
                alert(`오류: ${e.response.data.message}`);
            } else {
                alert("메모 저장 실패");
            }
        }
    }

    const toggleEdit = (id) => {
        setEditingMemo(prev => ({ ...prev, [id]: true }));
    };

    if (bookmarks.length === 0) {
        return <p>찜한 뉴스가 없습니다.</p>;
    }

    return (
        <div className="bookmark-page">
            <h2>내가 찜한 뉴스</h2>
            <div className="news-section">
                <NewsList
                    articles={bookmarks}
                    bookmarkedUrls={bookmarkedUrls}
                    handleBookmark={handleBookmark}
                    showSlider={true}
                    saveMemo={saveMemo}
                    memos={memos}
                    showMemo={true}
                    handleMemoChange={handleMemoChange}
                    editingMemo={editingMemo}
                    toggleEdit={toggleEdit}
                />
            </div>
        </div>
    );
}

export default BookmarkList;

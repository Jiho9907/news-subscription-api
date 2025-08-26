import { useEffect, useState } from "react";
import axiosInstance from "../../api/axiosInstance.js";
import NewsList from "../../hooks/NewsList.jsx";
import './BookmarkList.css';
import '../News/RecommendedNews.css';

function BookmarkList() {
    const [bookmarks, setBookmarks] = useState([]);
    const [bookmarkedUrls, setBookmarkedUrls] = useState([]);

    useEffect(() => {
        const fetchBookmarks = async () => {
            try {
                const res = await axiosInstance.get(`/bookmarks/list`);
                setBookmarks(res.data.data);

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
                />
            </div>
        </div>
    );
}

export default BookmarkList;

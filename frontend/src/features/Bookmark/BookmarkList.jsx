import {useEffect, useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";

function BookmarkList() {
    const [bookmarks, setBookmarks] = useState([]);

    useEffect(() => {
        const fetchBookmarks = async () => {
            const res = await axiosInstance.get(`/api/bookmarks/list`);
            setBookmarks(res.data.data);
        }
        fetchBookmarks();
    },[]);

    return (
        <div className="bookmark-page">
            <h2>내가 찜한 뉴스</h2>
            <div className="bookmark-list">
                {bookmarks.map((bookmark, idx) => (
                    <div className="bookmark-card" key={idx}>
                        <a href={bookmark.url} target="_blank" rel="noreferrer">
                            <h4 dangerouslySetInnerHTML={{ __html: bookmark.title }} />
                        </a>
                        <p>{bookmark.description}</p>
                        <small>{new Date(bookmark.pubDate).toLocaleString()}</small>
                    </div>
                ))}
            </div>
        </div>
    )
}
export default BookmarkList;
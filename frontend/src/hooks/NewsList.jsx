import '../features/News/RecommendedNews.css'
import {useState} from "react";

function truncateHTML(htmlString, maxLength) {
    const tempDiv = document.createElement("div");
    tempDiv.innerHTML = htmlString;
    const text = tempDiv.textContent || tempDiv.innerText || "";
    return text.length > maxLength ? text.slice(0, maxLength) + "..." : text;
}

const NewsList = ({
                      articles,
                      bookmarkedUrls,
                      handleBookmark,
                      showSlider = false,
                      saveMemo,
                      memos = {},
                      showMemo = false,
                      handleMemoChange,
                      editingMemo = {},
                      toggleEdit,
                  }) => {

    const [slideIndex, setSlideIndex] = useState(0);
    const visibleArticles = showSlider
        ? articles.slice(slideIndex, slideIndex + 3)
        : articles;

    const handleNext = () => {
        const next = slideIndex + 3;
        if (next < articles.length) {
            setSlideIndex(next);
        }
    };

    const handlePrev = () => {
        const prev = slideIndex - 3;
        if (prev >= 0) {
            setSlideIndex(prev);
        }
    };

    return (
        <div className="news-list">
            {showSlider && (
                <button onClick={handlePrev}
                        className="prev-button"
                        style={{ visibility: slideIndex > 0 ? 'visible' : 'hidden' }}>
                    ◀
                </button>
            )}

            {visibleArticles.map((article, idx) => {
                const url = article.url || article.link;
                const isBookmarked = bookmarkedUrls.includes(url);
                const memoValue = memos[article.id] || '';
                const isEditing = editingMemo[article.id];

                return (
                    <div className="news-card" key={idx}>
                        <div className="news-content">
                            <a href={url} target="_blank" rel="noreferrer">
                                <h4 dangerouslySetInnerHTML={{ __html: article.title }} />
                            </a>
                            <p dangerouslySetInnerHTML={{ __html: truncateHTML(article.description, 150) }} />
                            <small className="pub-date">
                                발행일 : {new Date(article.pubDate).toLocaleString()}
                            </small>
                            <button
                                className="bookmark-button"
                                onClick={() => handleBookmark(article)}
                            >
                                {isBookmarked ? "❤️" : "🤍"}
                            </button>

                            {showMemo && (
                                <div className="memo-section">
                                    {isEditing ? (
                                        <>
                                            <textarea
                                                value={memoValue}
                                                onChange={e =>
                                                    handleMemoChange(article.id, e.target.value)
                                                }
                                                placeholder="메모를 입력하세요"
                                            />
                                            <button onClick={() => saveMemo(article.id)}>저장</button>
                                        </>
                                    ) : (
                                        <>
                                            <p style={{ whiteSpace: 'pre-wrap' }}>
                                                {memoValue || '메모 없음'}
                                            </p>
                                            <button onClick={() => toggleEdit(article.id)}>
                                                {memoValue ? '수정' : '작성'}
                                            </button>
                                        </>
                                    )}
                                </div>
                            )}
                        </div>
                    </div>
                );
            })}

            {showSlider && (
                <button onClick={handleNext}
                        className="next-button"
                        style={{ visibility: slideIndex + 3 < articles.length ? 'visible' : 'hidden' }}>
                    ▶
                </button>
            )}
        </div>
    );
};

export default NewsList;

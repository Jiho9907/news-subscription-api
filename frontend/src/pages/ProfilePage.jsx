import React from 'react';
import MyProfile from '../features/user/MyProfile';
import Logout from '../features/auth/Logout';
import {useNavigate} from "react-router-dom";
import BookmarkList from "../features/Bookmark/BookmarkList.jsx";

function ProfilePage() {
    const navigate = useNavigate();

    const goToNews = () => {
        navigate("/news");
    };

    return (
        <div>
            <h1>내 프로필</h1>
            <MyProfile />

            <hr style={{ margin: "2rem 0" }} />

            <button onClick={goToNews}>
                뉴스 페이지로 이동
            </button>
            <BookmarkList />
            <Logout />
        </div>
    );
}

export default ProfilePage;

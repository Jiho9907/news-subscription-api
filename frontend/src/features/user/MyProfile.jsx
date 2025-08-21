import {useEffect, useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";
import {useAuth} from "../../auth/useAuth.jsx";
import './MyProfile.css'

function MyProfile() {
    const [user, setUser] = useState(null);
    const { isAuthLoading, isLoggedIn } = useAuth();

    useEffect(() => {
        if(isAuthLoading || !isLoggedIn)  return; // 아직 로딩 중이거나 로그인 안됨

        (async () => {
            try {
                const res = await axiosInstance.get('/user/me');
                setUser(res.data.data);
            } catch (err) {
                console.log("프로필 조회 실패", err);
            }
        })();
    },[isAuthLoading, isLoggedIn]);

    if(isAuthLoading)  return <p>인증 확인 중...</p>;
    if (!user) return <p>로딩중...</p>;

    return (
        <div className={"profile-container"}>
            <h2>내 정보</h2>
            <p><strong>ID: </strong>{user.id}</p>
            <p><strong>이메일:</strong> {user.email}</p>
            <p><strong>닉네임: </strong>{user.nickname}</p>
            <p><strong>가입방식:</strong> {user.provider}</p>
            <p><strong>역할:</strong> {user.role}</p>
        </div>
    )
}
export default MyProfile;
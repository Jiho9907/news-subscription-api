import {useEffect, useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";
import {useAuth} from "../../auth/useAuth.jsx";

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
        <div>
            <h2>내 정보</h2>
            <p>ID: {user.id}</p>
            <p>이메일: {user.email}</p>
            <p>닉네임: {user.nickname}</p>
            <p>가입방식: {user.provider}</p>
            <p>역할: {user.role}</p>
        </div>
    )
}
export default MyProfile;
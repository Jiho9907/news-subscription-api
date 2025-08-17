import {useEffect, useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";
import {get} from "axios";

function MyProfile() {
    const [user, setUser] = useState(null);

    useEffect(() => {
        (async () => {
            try {
                const res = await axiosInstance.get('/user/me');
                setUser(res.data.data);
            } catch (err) {
                console.log("프로필 조회 실패", err);
            }
        })();
    },[])

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
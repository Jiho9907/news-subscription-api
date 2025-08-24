import {useAuth} from "../auth/useAuth.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";

function OAuth2RedirectPage() {
    const navigate = useNavigate();
    const { login } = useAuth();

    useEffect(() => {
        const query = new URLSearchParams(window.location.search);
        const accessToken = query.get('accessToken');

        if (accessToken) {
            login(accessToken);           // 토큰 저장
            navigate('/profile');         // 메인으로 이동
        } else {
            navigate('/login');           // 실패 시 로그인 페이지
        }
    }, []);

    return <p>로그인 처리 중입니다...</p>;
}
export default OAuth2RedirectPage;
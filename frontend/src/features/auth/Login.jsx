import {useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../../auth/useAuth.jsx";
import './Login.css'

function Login() {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate(); // 리다이렉션 용도

    // 로그인 폼 제출
    const handleSubmit = async (e) => {
        e.preventDefault(); // 새로고침 방지

        try {
            // 로그인 API 호출
            const res = await axiosInstance.post('/auth/login', {
                email,
                password
            })

            // 응답에서 access 토큰 추출
            const { accessToken } = res.data.data;

            // useAuth의 login() 사용
            login(accessToken);

            // 로그인 성공 → 내 프로필로 이동
            navigate('/profile');

        } catch (err) {
            setMessage(err.response?.data?.message || '로그인 실패');
        }
    };

    return (
        <div className={"login-container"}>
            <h2>로그인</h2>

            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="이메일"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="비밀번호"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    required
                />
                <button type="submit">로그인</button>
            </form>
            {/* 실패 메시지 출력 */}
            {message && <p>{message}</p>}
            <button
                className="google-login-button"
                onClick={() => {
                    // 백엔드에서 OAuth2 요청 시작
                    window.location.href = 'http://localhost:8080/api/oauth2/authorization/google';
                }}
            >
                <img src="../../../public/google-logo.png" alt="Google Logo" />
                <span className="button-text">구글로 로그인</span>
            </button>
        </div>
    )
}
export default Login;
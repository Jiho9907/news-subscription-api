import {useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";
import {useNavigate} from "react-router-dom";

function Login( onLoginSuccess ) {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
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

            // accessToken LocalStorage에 저장 (임시 클라이언트 보관용)
            localStorage.setItem('accessToken', accessToken);

            // axios 기본 헤더에 토큰 설정 (재요청 시 자동 포함)
            // --로그인 직후 바로 다른 요청으로 인터셉터 비활성화시 임시용임--
            axiosInstance.defaults.headers.Authorization = `Bearer ${accessToken}`;

            // 로그인 성공 시 상위 컴포넌트에 알림
            onLoginSuccess();
            // 로그인 성공 → 내 프로필로 이동
            navigate('/profile');

        } catch (err) {
            setMessage(err.response?.data?.message || '로그인 실패');
        }
    };

    return (
        <div>
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
        </div>
    )
}
export default Login;
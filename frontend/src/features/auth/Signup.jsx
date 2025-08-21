import {useState} from "react";
import axiosInstance from "../../api/axiosInstance.js";
import './Signup.css'

function Signup() {

    // 입력 필드 상태관리
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [nickname, setNickname] = useState('');
    const [message, setMessage] = useState('');
    // const [loading, setLoading] = useState(true);
    // const [error, setError] = useState(false);

    // 폼 제출 이벤트 핸들러
    const handleSubmit = async (e) => {
        e.preventDefault(); // 페이지 새로고침 막기

        try{
          // 회원가입 API 호출
          // Post api/auth/signup
          const res = await axiosInstance.post('/auth/signup', {
              email,
              password,
              nickname
          });

          // 성공 시 서버에서 받은 메시지 출력
          setMessage(res.data.data);
        } catch (err) {
            // 실패 시 에러 메시지 출력
            setMessage(err.response?.data?.message || '회원가입 실패');
        }
    }

    return (
        <div className={"signup-container"}>
            <h2>회원가입</h2>
            {/* 회원가입 폼 */}
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

                <input
                    type="text"
                    placeholder="닉네임"
                    value={nickname}
                    onChange={e => setNickname(e.target.value)}
                    required
                />

                <button type="submit">회원가입</button>
            </form>

            {/* 결과 메시지 출력 */}
            {message && <p>{message}</p>}
        </div>
    );
}
export default Signup;
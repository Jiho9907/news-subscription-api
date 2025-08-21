import axiosInstance from "../../api/axiosInstance.js";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../../auth/useAuth.jsx";
import './Logout.css'

function Logout() {
    const { logout } = useAuth();
    const navigate = useNavigate();

    // 로그아웃 처리
    const handleLogout = async () => {
        try {
            // 백엔드에 로그아웃 요청
            await axiosInstance.post('/auth/logout');
        } catch (err) {
            console.error("로그아웃 실패",err);
        } finally {
            logout();

            // 로그인 페이지로 이동
            navigate('/login');
        }
    }
    return <button className={"logout-button"} onClick={handleLogout}>로그아웃</button>
}
export default Logout;
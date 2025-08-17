import axiosInstance from "../../api/axiosInstance.js";

function Logout({onLogoutSuccess}) {

    // 로그아웃 처리
    const handleLogout = async () => {
        try {
            // 백엔드에 로그아웃 요청
            await axiosInstance.post('/auth/logout');

            // 로컬 accessToken 삭제
            localStorage.removeItem('accessToken');

            // axios 기본 Authorization 헤더 제거
            delete axiosInstance.defaults.headers.Authorization;

            // 상위 컴포넌트에 로그아웃 완료 알림
            onLogoutSuccess();
        } catch (err) {
            console.error("로그아웃 실패",err);
        }

        return <button onClick={handleLogout}>로그아웃</button>
    }
}
export default Logout;
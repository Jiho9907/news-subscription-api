import {Navigate, Route, Routes} from "react-router-dom";
import ProtectedRoute from "../auth/ProtectedRoute.jsx";
import LoginPage from "../pages/LoginPage.jsx";
import SignupPage from "../pages/SignupPage.jsx";
import ProfilePage from "../pages/ProfilePage.jsx";
import PublicRoute from "../auth/PublicRoute.jsx";
import '../styles/global.css'
import OAuth2RedirectPage from "../pages/OAuth2RedirectPage.jsx";
import NewsPage from "../pages/NewsPage.jsx";
import {useAuth} from "../auth/useAuth.jsx";

function AppRouter() {
    const { isLoggedIn } = useAuth();

    // 아직 로그인 여부 판단 중이라면 로딩 중 상태 반환 (optional)
    if (isLoggedIn === null) {
        return <div>로딩 중...</div>;
    }

    return (
        <Routes>
            {/* 로그인 상태에 따라 "/" 경로 분기 */}
            <Route path="/" element={
                isLoggedIn ? <Navigate to="/news" /> : <Navigate to="/login" />
            } />

            {/* 비로그인 사용자 페이지 */}
            <Route path="/login" element={
                <PublicRoute>
                    <LoginPage />
                </PublicRoute>
            } />
            <Route path="/signup" element={
                <PublicRoute>
                    <SignupPage />
                </PublicRoute>
            } />

            <Route path="/oauth2/redirect" element={<OAuth2RedirectPage />} />

            {/* 로그인 사용자 페이지 */}
            <Route
                path="/profile"
                element={
                    <ProtectedRoute>
                        <ProfilePage />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/news"
                element={
                    <ProtectedRoute>
                        <NewsPage />
                    </ProtectedRoute>
                }
            />

            {/*  페이지 추가예정   */}

            {/* 정의되지 않은 경로는 홈으로 리디렉트 */}
            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    )
}
export default AppRouter;
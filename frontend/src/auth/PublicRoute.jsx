import { Navigate } from "react-router-dom";
import { useAuth } from "./useAuth.jsx";

function PublicRoute({ children }) {
    const { isLoggedIn, isAuthLoading } = useAuth();

    if (isAuthLoading) {
        return <div>인증 상태 확인 중...</div>;
    }

    if (isLoggedIn) {
        return <Navigate to="/news" />;
    }

    return children;
}

export default PublicRoute;

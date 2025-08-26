import {useAuth} from "./useAuth.jsx";
import {Navigate} from "react-router-dom";

function ProtectedRoute({ children }) {
    const { isLoggedIn, isAuthLoading } = useAuth();

    if (isAuthLoading) {
        return <div>인증 상태 확인 중...</div>;
    }

    if (!isLoggedIn) {
        return <Navigate to="/login" />;
    }

    return children;
}

export default ProtectedRoute;
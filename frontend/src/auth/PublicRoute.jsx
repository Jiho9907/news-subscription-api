import { Navigate } from "react-router-dom";
import { useAuth } from "./useAuth";

function PublicRoute({ children }) {
    const { isLoggedIn } = useAuth();

    // 로그인되어 있다면 다른 곳으로 리다이렉션
    return isLoggedIn ? <Navigate to="/profile" /> : children;
}

export default PublicRoute;

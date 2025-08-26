import { Navigate } from "react-router-dom";
import { useAuth } from "../auth/useAuth.jsx";

function RedirectHome() {
    const { isLoggedIn, isAuthLoading } = useAuth();

    console.log("RedirectHome", { isLoggedIn, isAuthLoading });
    if (isAuthLoading) {
        return <div>라우팅 중...</div>;
    }

    return <Navigate to={isLoggedIn ? "/news" : "/login"} />;
}

export default RedirectHome;

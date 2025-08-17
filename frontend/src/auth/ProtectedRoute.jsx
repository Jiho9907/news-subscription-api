import {Navigate} from "react-router-dom";
import {useAuth} from "./useAuth.js";

function ProtectedRoute({ children }) {
    const { isLoggedIn } = useAuth();
    return isLoggedIn ? children : <Navigate to={'/login'}/>;
}
export default ProtectedRoute;
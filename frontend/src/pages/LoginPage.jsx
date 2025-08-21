import React from 'react';
import Login from "../features/auth/Login.jsx";
import {useNavigate} from "react-router-dom";

function LoginPage() {
    const navigate = useNavigate();
    return (
        <div>
            <Login onLoginSuccess={() => navigate('/profile')} />
        </div>
    );
}

export default LoginPage;
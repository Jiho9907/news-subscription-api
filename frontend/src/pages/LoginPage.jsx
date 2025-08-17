import React from 'react';
import Login from "../features/auth/Login.jsx";

function LoginPage() {
    return (
        <div>
            <h1>로그인</h1>
            <Login onLoginSuccess={() => window.location.href = '/profile'} />
        </div>
    );
}

export default LoginPage;
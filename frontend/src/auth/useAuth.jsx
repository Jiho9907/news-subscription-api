import {createContext, useContext, useEffect, useState} from "react";
import axiosInstance from "../api/axiosInstance.js";

// context 생성
const AuthContext = createContext();

// provider 컴포넌트
export function AuthProvider({ children }) {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isAuthLoading, setIsAuthLoading] = useState(true);

    // accessToken 저장 + axios 기본 헤더 설정
    const login = (token) => {
        // accessToken LocalStorage에 저장 (임시 클라이언트 보관용)
        localStorage.setItem('accessToken', token);

        // axios 기본 헤더에 토큰 설정 (재요청 시 자동 포함)
        // --로그인 직후 바로 다른 요청으로 인터셉터 비활성화시 임시용임--
        axiosInstance.defaults.headers.Authorization = `Bearer ${token}`;
        setIsLoggedIn(true);
    }

    // accessToken 삭제 + axios 기본 헤더 제거
    const logout = () => {
        // 로컬 accessToken 삭제
        localStorage.removeItem('accessToken');

        // axios 기본 Authorization 헤더 제거
        delete axiosInstance.defaults.headers.Authorization;
        setIsLoggedIn(false);
    }

    // 첫 진입 시 accessToken 확인 및 axios 기본 헤더 세팅
    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            axiosInstance.defaults.headers.Authorization = `Bearer ${token}`;
            setIsLoggedIn(true);
        }
        setIsAuthLoading(false);
    }, []);
    
    return (
        <AuthContext.Provider value={{isLoggedIn, login, logout, isAuthLoading }}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth() {
    return useContext(AuthContext);
}
import axios from "axios";

// Axios 인스턴스 생성
const axiosInstance = axios.create({

    // 백엔드 API 기본 주소 설정
    baseURL: process.env.REACT_APP_API_URL,

    // 브라우저가 HttpOnly RefreshToken 자동 저장/포함하여 백엔드로 보내도록 허용
    withCredentials: true,
});
// 요청 전 AccessToken 자동 삽입 인터셉트
axiosInstance.interceptors.request.use((config) => {
    // 로그인 시 저장된 AccessToken 가져오기
    const token = localStorage.getItem('accessToken');

    // 모든 요청에 Authorization 헤더로 JWT 자동 포함
    if(token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

export default axiosInstance;
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

// 응답 인터셉터 : 401 -> accessToken 재발급
axiosInstance.interceptors.response.use(
    (response) => response, // 성공 응답은 그대로
    async (error) => {
        const originalRequest = error.config;

        // accessToken이 만료됐고, 재시도 한 적 없는 경우
        if (
            error.response?.status === 401 &&
            !originalRequest._retry
        ) {
            originalRequest._retry = true;

            try {
                // refreshToken 기반으로 새 accessToken 발급 요청
                const res = await axios.post(
                    `${process.env.REACT_APP_API_URL}/auth/refresh`,
                    {},
                    { withCredentials: true }
                );

                const newAccessToken = res.data.data.accessToken;
                // 새 토큰 저장
                localStorage.setItem('accessToken', newAccessToken);
                axiosInstance.defaults.headers.Authorization = `Bearer ${newAccessToken}`;
                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;

                // 원래 요청 재시도
                return axiosInstance(originalRequest);
            }  catch (refreshErr) {
                console.error('토큰 재발급 실패:', refreshErr);
                localStorage.removeItem('accessToken');
                delete axiosInstance.defaults.headers.Authorization;
                window.location.href = '/login'; // 강제 로그아웃
            }
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;
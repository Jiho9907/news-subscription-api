import React from 'react';
import MyProfile from '../features/user/MyProfile';
import Logout from '../features/auth/Logout';

function ProfilePage() {
    return (
        <div>
            <h1>내 프로필</h1>
            <MyProfile />
            <Logout onLogoutSuccess={() => window.location.href = '/login'} />
        </div>
    );
}

export default ProfilePage;

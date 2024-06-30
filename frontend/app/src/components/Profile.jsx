import { useState, useEffect } from 'react';
import axios from 'axios';

import './Profile.css';
import defaultProfile from "./../assets/profile.png"

const Profile = () => {
  const [user, setUser] = useState(null);
  const token = localStorage.getItem('token');
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await axios.get(`${apiUrl}/api/v1/users`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.status === 200) {
          setUser(response.data);
        }
      } catch (error) {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    };

    fetchUser();
  }, [token]);

  return (
    <div className="profile">
      <img src={user?.imageUrl || defaultProfile} alt="프로필 이미지" />
      <div className="info">
        <div className="names">
          <div className="name">{user?.name || '이름'}</div>
          <div className="nickname">{user?.nickname || '닉네임'}</div>
        </div>
        <div className="email">{user?.email || '이메일'}</div>
      </div>
    </div>
  );
}

export default Profile;

import { useEffect, useState } from 'react';
import './Sidebar.css';
import axios from 'axios';
import profile from "./../assets/profile.png";
import Button from "./Button";
import { RiMessage2Line } from "react-icons/ri";
import { FaRegCalendarAlt } from "react-icons/fa";
import { LuVote } from "react-icons/lu";

const Sidebar = ({ gatheringId, activeTab, setActiveTab }) => {
  const token = localStorage.getItem('token');
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [friends, setFriends] = useState([]);

  useEffect(() => {
    const fetchFriends = async () => {
      try {
        const response = await axios.get(`${apiUrl}/api/v1/friends/`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.status === 200) {
          setFriends(response.data);
        }
      } catch (error) {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    };

    fetchFriends();
  }, [token]);

  const inviteFriend = async (friendId) => {
    try {
      const response = await axios.post(`${apiUrl}/api/v1/friends/invite`, {
        gatheringId,
        friendId
      },{
        headers: {
          'Authorization': `Bearer ${token}`
        },
        params: {
            gatheringId,
            friendId
        }
      });
      if (response.status === 201) {
        alert('친구 초대되었습니다.');
      }
    } catch (error) {
      if (error.response && error.response.status === 403) {
        alert('사용자와 친구 정보가 일치하지 않습니다.');
      } else if (error.response.status === 409) { 
        alert('이미 초대한 친구입니다.');
      } else {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    }
  }

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  return (
    <div className="sidebar">
      <div className="sidebar-header">
        <h2>운동 모임</h2>
      </div>
      <div className="sidebar-menu">
        <div className="sidebar-item">
          <img src={profile} alt="프로필 이미지" />
          <span>방장_id</span>
        </div>
        <div className="sidebar-links">
          <p
            className={activeTab === 'Messages' ? 'active' : ''}
            onClick={() => setActiveTab('Messages')}
          >
            <RiMessage2Line/>
            {" 대화하기"}
          </p>
          <p
            className={activeTab === 'Calendar' ? 'active' : ''}
            onClick={() => setActiveTab('Calendar')}
          >
            <FaRegCalendarAlt/>
            {" 일정잡기"}
          </p>
          <p
            className={activeTab === 'Votes' ? 'active' : ''}
            onClick={() => setActiveTab('Votes')}
          >
            <LuVote/>
            {" 투표하기"}
          </p>
        </div>
        <div className="sidebar-participants">
          <h3>모임원</h3>
          <div className="participant-item">문진수</div>
          <div className="participant-item">윤대현</div>
        </div>
        <div className="invite-button-container">
          <Button text={"친구초대"} onClick={openModal}/>
          {isModalOpen && (
            <div className="invite-modal">
              <h3>친구 초대하기</h3>
              <div className="friend-list">
                <ul>
                  {friends.map(friend => (
                    <li key={friend.id}>
                      <span className="friend-name">{friend.nickName}</span>
                      <button onClick={() => inviteFriend(friend.userId)} className="invite-button">초대</button>
                    </li>
                  ))}
                </ul>
              </div>
              <div className="close-button">
                <Button text={'닫기'} onClick={closeModal} />
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Sidebar;

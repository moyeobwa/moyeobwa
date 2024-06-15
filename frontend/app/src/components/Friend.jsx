import { useEffect, useState } from 'react';
import Modal from 'react-modal';
import axios from 'axios';
import Button from './Button';
import './Friend.css';

Modal.setAppElement('#root'); // Replace '#root' with your actual root element ID

const Friend = ({ isOpen, onRequestClose }) => {
  const token = localStorage.getItem('token');
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [activeTab, setActiveTab] = useState('friendList');
  const [friends, setFriends] = useState([]);
  const [searchFriend, setSearchFriend] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [friendRequests, setFriendRequests] = useState([]);
  const [pendingFriendRequests, setPendingFriendRequests] = useState([]);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [friendToDelete, setFriendToDelete] = useState(null);

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

    const fetchFriendRequests = async () => {
      try {
        const response = await axios.get(`${apiUrl}/api/v1/friends/requests/user`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.status === 200) {
          setFriendRequests(response.data);
        }
      } catch (error) {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    };

    const fetchPendingFriendRequests = async () => {
      try {
        const response = await axios.get(`${apiUrl}/api/v1/friends/requests/friend`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.status === 200) {
          setPendingFriendRequests(response.data);
        }
      } catch (error) {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    };

    fetchFriends();
    fetchFriendRequests();
    fetchPendingFriendRequests();
  }, [token]);

  const handleSearchFriend = async () => {
    try {
      const response = await axios.get(`${apiUrl}/api/v1/users/search`, {
        params: {
          nickname: searchFriend
        },
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.status === 200) {
        setSearchResults(response.data);
      } else {
        console.error('Failed to fetch user data');
        alert('사용자 정보를 가져오는 데 실패했습니다.');
      }
    } catch (error) {
      if (error.response && error.response.status === 404) {
        setSearchResults([]);
      } else {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    }
  };

  const handleAddFriend = async (id) => {
    try {
      const response = await axios.post(
        `${apiUrl}/api/v1/friends/request/${id}`,
        null,
        {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }
      );

      if (response.status === 200) {
        alert("친구 신청이 완료되었습니다.");
        const updatedFriendRequests = await axios.get(`${apiUrl}/api/v1/friends/requests/user`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (updatedFriendRequests.status === 200) {
          setFriendRequests(updatedFriendRequests.data);
        }
      }
    } catch (error) {
      if (error.response && error.response.status === 409) {
        alert('이미 친구 또는 친구 요청 상태입니다.');
      } else if (error.response && error.response.status === 400) {
        alert('자기 자신에게 친구 신청을 할 수 없습니다.');
      } else {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    }
  };

  const handleDeleteFriend = async () => {
    try {
      const response = await axios.delete(`${apiUrl}/api/v1/friends/delete/${friendToDelete}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (response.status === 200) {
        setFriends(friends.filter(friend => friend.id !== friendToDelete));
        closeDeleteModal();
      }
    } catch (error) {
      if (error.response && error.response.status === 404) {
        alert('친구를 찾을 수 없습니다.');
      } else if(error.response && error.response.status === 403) {
        alert('사용자와 친구 정보가 일치하지 않습니다.');
      } else {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    }
  };

  const handleCancelRequest = async (id) => {
    try {
      const response = await axios.delete(`${apiUrl}/api/v1/friends/cancel/${id}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (response.status === 200) {
        setFriendRequests(friendRequests.filter(request => request.id !== id));
        alert('친구 신청이 취소되었습니다.');
      }
    } catch (error) {
      if (error.response && error.response.status === 403) {
        alert('사용자와 친구 정보가 일치하지 않습니다.');
      } else {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    }
  };

  const handleAcceptRequest = async (id) => {
    try {
      const response = await axios.post(`${apiUrl}/api/v1/friends/accept/${id}`, null, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (response.status === 200) {
        setFriends([...friends, response.data]);
        setPendingFriendRequests(pendingFriendRequests.filter(request => request.id !== id));
        alert(`${response.data.nickName}님과 친구가 되었습니다.`);
      }
    } catch (error) {
      if (error.response && error.response.status === 403) {
        alert('사용자와 친구 정보가 일치하지 않습니다.');
      } else {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    }
  };

  const handleRejectRequest = async (id) => {
    try {
      const response = await axios.delete(`${apiUrl}/api/v1/friends/reject/${id}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (response.status === 200) {
        setPendingFriendRequests(pendingFriendRequests.filter(request => request.id !== id));
        alert('친구 요청을 거절하였습니다.');
      }
    } catch (error) {
      if (error.response && error.response.status === 403) {
        alert('사용자와 친구 정보가 일치하지 않습니다.');
      } else {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    }
  };

  const handleKeyPress = (event) => {
    if (event.key === 'Enter' && searchFriend.trim() !== '') {
      handleSearchFriend();
    }
  };

  const handleTabClick = (tab) => {
    setActiveTab(tab);
    if (tab === 'friendList') {
      setSearchFriend('');
      setSearchResults([]);
    }
  };

  const openDeleteModal = (friendId) => {
    setFriendToDelete(friendId);
    setIsDeleteModalOpen(true);
  };

  const closeDeleteModal = () => {
    setFriendToDelete(null);
    setIsDeleteModalOpen(false);
  };

  const renderContent = () => {
    if (activeTab === 'friendList') {
      return (
        <div className="friend-list">
          <ul>
            {friends.map(friend => (
              <li key={friend.id}>
                <span className="friend-name">{friend.nickName}</span>
                <button onClick={() => openDeleteModal(friend.id)} className="delete-button">삭제</button>
              </li>
            ))}
          </ul>
        </div>
      );
    } else if (activeTab === 'friendRequest') {
      return (
        <div className="friend-request">
          <div className="friend-request-request">
            <input 
              type="text" 
              placeholder="친구 닉네임" 
              className="input-nickname"
              value={searchFriend}
              onChange={(e) => setSearchFriend(e.target.value)} 
              onKeyPress={handleKeyPress}
            />
            <button 
              type="submit" 
              className="request-button" 
              onClick={handleSearchFriend}
              disabled={searchFriend.trim() === ''}
            >
              검색
            </button>
          </div>
          {searchResults.length > 0 ? (
            <div className="search-results">
              <ul>
                {searchResults.map(user => (
                  <li key={user.id}>
                    <span className="friend-name">{user.nickname}</span>
                    <button onClick={() => handleAddFriend(user.id)} className="add-friend-button">친구 신청</button>
                  </li>
                ))}
              </ul>
            </div>
          ) : (
            <p className="null-friend">사용자가 존재하지 않습니다.</p>
          )}
          <div className="friend-request-list">
            <h3>친구 신청 목록</h3>
            <ul>
              {friendRequests.map(request => (
                <li key={request.id}>
                  <span className="friend-name">{request.nickName}</span>
                  <button onClick={() => handleCancelRequest(request.id)} className="cancel-button">취소</button>
                </li>
              ))}
            </ul>
          </div>
        </div>
      );
    } else if (activeTab === 'acceptRequest') {
      return (
        <div className="friend-accept-list">
          <h3>친구 수락 요청</h3>
          <ul>
            {pendingFriendRequests.map(request => (
              <li key={request.id}>
                <span className="friend-name">{request.nickName}</span>
                <div>
                  <button onClick={() => handleAcceptRequest(request.id)} className="accept-button">수락</button>
                  <button onClick={() => handleRejectRequest(request.id)} className="reject-button">거절</button>
                </div>
              </li>
            ))}
          </ul>
        </div>
      );
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="Friend List Modal"
      className="modal"
      overlayClassName="overlay"
    >
      <div className="modal-tabs">
        <button
          className={activeTab === 'friendList' ? 'active' : ''}
          onClick={() => handleTabClick('friendList')}
        >
          친구 목록
        </button>
        <button
          className={activeTab === 'friendRequest' ? 'active' : ''}
          onClick={() => handleTabClick('friendRequest')}
        >
          친구 신청
        </button>
        <button
          className={activeTab === 'acceptRequest' ? 'active' : ''}
          onClick={() => handleTabClick('acceptRequest')}
        >
          친구 수락
        </button>
      </div>
      <div className="modal-content">
        {renderContent()}
      </div>
      <div className="close-button">
        <Button text={'닫기'} onClick={onRequestClose} />
      </div>
      
      <Modal
        isOpen={isDeleteModalOpen}
        onRequestClose={closeDeleteModal}
        contentLabel="Delete Friend Confirmation"
        className="delete-modal"
        overlayClassName="overlay"
      >
        <div className="card">
          <div className="header">
            <div className="image">
              <svg aria-hidden="true" stroke="currentColor" strokeWidth="1.5" viewBox="0 0 24 24" fill="none">
                <path d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z" strokeLinejoin="round" strokeLinecap="round"></path>
              </svg>
            </div>
            <div className="content">
              <span className="title">친구 삭제</span>
              <p className="message">정말로 친구를 삭제하시겠습니까? <br/>이 작업은 취소할 수 없습니다.</p>
            </div>
            <div className="actions">
              <button className="delete" type="button" onClick={handleDeleteFriend}>삭제</button>
              <button className="cancel" type="button" onClick={closeDeleteModal}>취소</button>
            </div>
          </div>
        </div>
      </Modal>
    </Modal>
  );
};

export default Friend;

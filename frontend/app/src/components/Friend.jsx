import { useState } from 'react';
import Modal from 'react-modal';
import './Friend.css';
import Button from './Button';

const friendData = [
  {
    id: 1,
    userId: 1234,
    nickName: "윤대현"
  },
  {
    id: 2,
    userId: 2345,
    nickName: "문진수"
  },
  {
    id: 3,
    userId: 3456,
    nickName: "홍길동"
  },
  {
    id: 4,
    userId: 5678,
    nickName: "케로로"
  }
];

const Friend = ({ isOpen, onRequestClose }) => {
  const [activeTab, setActiveTab] = useState('friendList');
  const [friends, setFriends] = useState(friendData);
  const [newFriend, setNewFriend] = useState('');

  const handleAddFriend = (event) => {
    event.preventDefault();
    if (newFriend.trim()) {
      const newFriendData = {
        id: friends.length + 1,
        userId: Math.floor(Math.random() * 10000),
        nickName: newFriend
      };
      setFriends([...friends, newFriendData]);
      setNewFriend('');
    }
  };

  const handleDeleteFriend = (id) => {
    setFriends(friends.filter(friend => friend.id !== id));
  };

  const renderContent = () => {
    if (activeTab === 'friendList') {
      return (
        <div className="friend-list">
          <ul>
            {friends.map(friend => (
              <li key={friend.id}>
                <span className="friend-name">{friend.nickName}</span>
                <button onClick={() => handleDeleteFriend(friend.id)} className="delete-button">삭제</button>
              </li>
            ))}
          </ul>
        </div>
      );
    } else if (activeTab === 'friendRequest') {
      return (
        <div className="friend-request">
          <form className="friend-request-request" onSubmit={handleAddFriend}>
            <input 
              type="text" 
              placeholder="친구 닉네임" 
              className="input-nickname"
              value={newFriend}
              onChange={(e) => setNewFriend(e.target.value)} 
            />
            <button type="submit" className="request-button">친구 신청</button>
          </form>
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
          onClick={() => setActiveTab('friendList')}
        >
          친구 목록
        </button>
        <button
          className={activeTab === 'friendRequest' ? 'active' : ''}
          onClick={() => setActiveTab('friendRequest')}
        >
          친구 신청
        </button>
      </div>
      <div className="modal-content">
        {renderContent()}
      </div>
      <div className="close-button">
        <Button text={'닫기'} onClick={onRequestClose} />
      </div>
    </Modal>
  );
};

export default Friend;

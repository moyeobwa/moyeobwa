import React, { useState } from 'react';
import axios from 'axios';
import './VoteModal.css';

const VoteModal = ({ gatheringId, onClose }) => {
  const [newVoteTitle, setNewVoteTitle] = useState('');
  const [newVoteOptions, setNewVoteOptions] = useState(['']);

  const token = localStorage.getItem('token');
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  const createVote = async () => {
    try {
      await axios.post(`${apiUrl}/api/v1/votes`, {
        title: newVoteTitle,
        gatheringId: gatheringId,
        optionNames: newVoteOptions
      }, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      setNewVoteTitle('');
      setNewVoteOptions(['']);
      onClose();
    } catch (error) {
      console.error(error);
      alert('네트워크 오류가 발생했습니다.');
    }
  };

  return (
    <div className="modal">
      <div className="modal-content">
        <h2>새 투표 생성</h2>
        <input
          type="text"
          value={newVoteTitle}
          onChange={(e) => setNewVoteTitle(e.target.value)}
          placeholder="투표 제목"
        />
        {newVoteOptions.map((option, index) => (
          <input
            key={index}
            type="text"
            value={option}
            onChange={(e) => {
              const updatedOptions = [...newVoteOptions];
              updatedOptions[index] = e.target.value;
              setNewVoteOptions(updatedOptions);
            }}
            placeholder="옵션 입력"
          />
        ))}
        <button onClick={() => setNewVoteOptions([...newVoteOptions, ''])}>옵션 추가</button>
        <button onClick={createVote}>투표 생성</button>
        <button onClick={onClose}>닫기</button>
      </div>
    </div>
  );
};

export default VoteModal;

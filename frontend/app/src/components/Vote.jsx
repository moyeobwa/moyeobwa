import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './Vote.css';
import VoteModal from './VoteModal';
import VoteOptionsModal from './VoteOptionsModal';

const Vote = ({ gatheringId }) => {
  const [votes, setVotes] = useState([]);
  const [selectedVote, setSelectedVote] = useState(null);
  const [voteResults, setVoteResults] = useState([]);
  const [selectedOption, setSelectedOption] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isVoteOptionsModalOpen, setIsVoteOptionsModalOpen] = useState(false);

  const token = localStorage.getItem('token');
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  useEffect(() => {
    fetchVotes();
  }, []);

  const fetchVotes = async () => {
    try {
      const response = await axios.get(`${apiUrl}/api/v1/votes/${gatheringId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      setVotes(response.data);
    } catch (error) {
      console.error(error);
      alert('네트워크 오류가 발생했습니다.');
    }
  };

  const fetchOptions = async (voteId) => {
    try {
      const response = await axios.get(`${apiUrl}/api/v1/options/${voteId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      setVoteResults(response.data);
      setSelectedVote(voteId);
      setIsVoteOptionsModalOpen(true);
    } catch (error) {
      console.error(error);
      alert('네트워크 오류가 발생했습니다.');
    }
  };

  const submitVote = async (optionId) => {
    try {
      await axios.post(`${apiUrl}/api/v1/votes/${selectedVote}/vote`, {
        optionId: optionId
      }, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      fetchOptions(selectedVote);
      alert('투표가 완료되었습니다.');
      setIsVoteOptionsModalOpen(false);
    } catch (error) {
      console.error(error);
      alert('네트워크 오류가 발생했습니다.');
    }
  };

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    fetchVotes();
  };

  const closeVoteOptionsModal = () => {
    setIsVoteOptionsModalOpen(false);
  };

  return (
    <div className="vote-container">
      <div className="vote-list-container">
        <h2 className="vote-header">투표하기</h2>
        <button className="vote-button" onClick={openModal}>새 투표 생성</button>
        {votes.map((vote) => (
          <div key={vote.id} className="vote-item" onClick={() => fetchOptions(vote.id)}>
            {vote.title}
          </div>
        ))}
      </div>
      {isModalOpen && <VoteModal gatheringId={gatheringId} onClose={closeModal} />}
      {isVoteOptionsModalOpen && (
        <VoteOptionsModal
          voteResults={voteResults}
          selectedOption={selectedOption}
          setSelectedOption={setSelectedOption}
          submitVote={submitVote}
          onClose={closeVoteOptionsModal}
        />
      )}
    </div>
  );
};

export default Vote;

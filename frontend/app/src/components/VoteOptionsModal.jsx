import React from 'react';
import './VoteOptionsModal.css';

const VoteOptionsModal = ({ voteResults, selectedOption, setSelectedOption, submitVote, onClose }) => {
  return (
    <div className="modal">
      <div className="modal-content">
        <h2>투표하기</h2>
        {voteResults.map((option) => (
          <div key={option.optionId}>
            <input
              type="radio"
              name="vote"
              value={option.optionId}
              onChange={() => setSelectedOption(option.optionId)}
            />
            {option.content}
          </div>
        ))}
        <button onClick={() => submitVote(selectedOption)}>투표</button>
        <button onClick={onClose}>닫기</button>
      </div>
    </div>
  );
};

export default VoteOptionsModal;

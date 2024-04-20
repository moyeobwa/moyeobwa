import React from 'react';

const QuestionCard = ({ image, question, onAnswer }) => {
  return (
    <div style={{ display: 'flex', padding: '20px', border: '1px solid #ccc', margin: '20px' }}>
      <img src={image} alt="Question" style={{ marginRight: '20px' }} />
      <div>
        <p>{question}</p>
        <input type="text" placeholder="이런 모임 어때요?" /* Implement the onAnswer function to handle answer logic */ />
      </div>
    </div>
  );
};

export default QuestionCard;
import React, { useState } from 'react';
import './Chat.css';

const Chat = () => {
  const [messages, setMessages] = useState([
    { user: 'user1', text: '퍼블리싱 너무 힘들어요.....', time: '10:30 AM' },
    { user: 'user2', text: "GPT 도와줘!!", time: '10:32 AM' }
  ]);
  const [newMessage, setNewMessage] = useState('');

  const handleSendMessage = () => {
    const newMsg = { user: 'You', text: newMessage, time: new Date().toLocaleTimeString() };
    setMessages([...messages, newMsg]);
    setNewMessage('');
  };

  return (
    <div className="chat-section">
      <div className="chat-messages">
        {messages.map((msg, index) => (
          <div key={index} className={`chat-message ${msg.user === 'You' ? 'chat-message-sent' : 'chat-message-received'}`}>
            <p><strong>{msg.user}</strong> • {msg.time}</p>
            <p>{msg.text}</p>
          </div>
        ))}
      </div>
      <div className="chat-input">
        <input 
          type="text" 
          value={newMessage} 
          onChange={(e) => setNewMessage(e.target.value)} 
          placeholder="Type your message..." 
        />
        <button onClick={handleSendMessage}>Send</button>
      </div>
    </div>
  );
};

export default Chat;

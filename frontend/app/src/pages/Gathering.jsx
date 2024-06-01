import React, { useState } from 'react';
import Sidebar from '../components/Sidebar';
import Chat from '../components/Chat';
import Calendar from '../components/Calendar/Calendar';
import Vote from '../components/Vote';
import Header from '../components/Header';
import './Gathering.css';

const Gathering = () => {
  const [activeTab, setActiveTab] = useState('Messages');

  return (
    <div className="gathering">
      <Header />
      <div className="main-content">
        <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />
        <div className="chat-section">
          {activeTab === 'Messages' && <Chat />}
          {activeTab === 'Calendar' && <Calendar />}
          {activeTab === 'Votes' && <Vote />}
        </div>
      </div>
    </div>
  );
};

export default Gathering;

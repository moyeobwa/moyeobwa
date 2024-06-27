import React, { useState } from 'react';
import Sidebar from '../components/Sidebar';
import Chat from '../components/Chat';
import Calendar from '../components/Calendar/Calendar';
import Vote from '../components/Vote';
import Header from '../components/Header';
import './Gathering.css';
import { useParams } from 'react-router-dom';

const Gathering = () => {
  const { id } = useParams();
  const [activeTab, setActiveTab] = useState('Messages');

  return (
    <div className="gathering">
      <Header />
      <div className="main-content">
        <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />
        <div className="content-section">
          {activeTab === 'Messages' && <Chat gatheringId={id} />}
          {activeTab === 'Calendar' && <Calendar gatheringId={id}/>}
          {activeTab === 'Votes' && <Vote gatheringId={id} />}
        </div>
      </div>
    </div>
  );
};

export default Gathering;

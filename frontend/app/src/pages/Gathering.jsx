import React from 'react';
import Sidebar from '../components/Sidebar';
import Chat from '../components/Chat'
import Header from '../components/Header';
import './Gathering.css';

const Gathering = () => {
  return (
    <div className="gathering">
      <Header />
      <div className="main-content">
        <Sidebar />
        <Chat />
      </div>
    </div>
  );
};

export default Gathering;

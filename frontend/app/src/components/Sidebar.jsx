import React from 'react';
import './Sidebar.css';

const Sidebar = () => {
  return (
    <div className="sidebar">
      <div className="sidebar-header">
        <h2>운동 모임</h2>
      </div>
      <div className="sidebar-menu">
        <div className="sidebar-item">
          <span>방장_id</span>
        </div>
        <div className="sidebar-links">
          <p>Messages</p>
          <p>Calendar</p>
          <p>Votes</p>
        </div>
        <div className="sidebar-participants">
          <h3>모임원</h3>
          <div className="participant-item">user1</div>
          <div className="participant-item">user2</div>
          <div className="participant-add">+</div>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;

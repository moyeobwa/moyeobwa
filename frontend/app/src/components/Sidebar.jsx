import React from 'react';
import './Sidebar.css';
import profile from "./../assets/profile.png"
import Button from "./Button"
import { RiMessage2Line } from "react-icons/ri";
import { FaRegCalendarAlt } from "react-icons/fa";
import { LuVote } from "react-icons/lu";

const Sidebar = ({ activeTab, setActiveTab }) => {
  return (
    <div className="sidebar">
      <div className="sidebar-header">
        <h2>운동 모임</h2>
      </div>
      <div className="sidebar-menu">
        <div className="sidebar-item">
          <img src={profile} alt="프로필 이미지" />
          <span>방장_id</span>
        </div>
        <div className="sidebar-links">
          <p
            className={activeTab === 'Messages' ? 'active' : ''}
            onClick={() => setActiveTab('Messages')}
          >
            <RiMessage2Line/>
            {" 대화하기"}
          </p>
          <p
            className={activeTab === 'Calendar' ? 'active' : ''}
            onClick={() => setActiveTab('Calendar')}
          >
            <FaRegCalendarAlt/>
            {" 일정잡기"}
          </p>
          <p
            className={activeTab === 'Votes' ? 'active' : ''}
            onClick={() => setActiveTab('Votes')}
          >
            <LuVote/>
            {" 투표하기"}
          </p>
        </div>
        <div className="sidebar-participants">
          <h3>모임원</h3>
          <div className="participant-item">문진수</div>
          <div className="participant-item">윤대현</div>
        </div>
        <Button text={"친구초대"}/>
      </div>
    </div>
  );
};

export default Sidebar;

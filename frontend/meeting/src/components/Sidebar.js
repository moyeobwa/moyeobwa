import React, { useState } from 'react';
import MyInfo from './MyInfo';
import GroupList from './GroupList';

const Sidebar = ({ userName, groups }) => {
    const [isCalendarOpen, setIsCalendarOpen] = useState(false);
  
    const toggleCalendar = () => {
      setIsCalendarOpen(!isCalendarOpen);
    };

  const sidebarStyle = {
    width: '250px',
    height: '100vh',
    borderRight: '1px solid #ccc',
    position: 'relative', // 상대적 위치 설정
  };

  const arrowButtonStyle = {
    position: 'absolute',
    top: '50%',
    right: isCalendarOpen ? '-40px' : '-40px', // 사이드바 바깥으로 위치 조정
    transform: 'translateY(-50%)', // 버튼을 수직 중앙으로 정렬
    cursor: 'pointer',
    border: 'none',
    background: '#fff',
    right: '-30px',
    zIndex: 30, // 다른 컨텐츠 위로
    transition: 'transform 0.3s ease-in-out',
  };

  const calendarStyle = {
    position: 'absolute',
    top: '25%',
    left: isCalendarOpen ? '300px' : '300px', // 달력을 사이드바의 완전 오른쪽으로 위치시킵니다.
    width: '700px', // 달력의 너비는 250px로 고정
    height: '500px',
    backgroundColor: '#f0f0f0',
    boxShadow: '0px 0 10px rgba(0,0,0,0.2)',
    transition: 'opacity 0.3s ease-in-out, visibility 0.3s ease-in-out, left 0.3s ease-in-out',
    opacity: isCalendarOpen ? 1 : 0,
    visibility: isCalendarOpen ? 'visible' : 'hidden',
    zIndex: 10,
  };

  return (
    <div style={{ position: 'relative', width: '250px' }}>
      <div style={sidebarStyle}>
        <MyInfo userName={userName} />
        <GroupList groups={groups} />
      </div>
      <div style={calendarStyle}>
        {/* 달력 컴포넌트 또는 내용 */}
      </div>
      <button onClick={toggleCalendar} style={arrowButtonStyle}>
        {isCalendarOpen ? '◀' : '▶'}
      </button>
    </div>
  );
};

export default Sidebar;
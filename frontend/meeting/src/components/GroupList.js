import React from 'react';

const GroupList = ({ groups }) => {
  return (
    <div>
      <h3>모임 목록</h3>
      <ul style={{ listStyleType: 'none', padding: 0, zIndex: 7 }}>
        {groups.map((group, index) => (
          <li key={index} style={{ padding: '10px 0' }}>
            {group.name}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default GroupList;

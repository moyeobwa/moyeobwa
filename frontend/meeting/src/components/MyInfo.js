import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserCircle } from '@fortawesome/free-solid-svg-icons';

const MyInfo = ({ userName }) => {
  return (
    <div style={{ textAlign: 'center', padding: '20px' }}>
      <div style={{ fontSize: '24px', marginBottom: '10px' }}>{`${userName}님, 환영합니다!`}</div>
      <div style={{
        borderRadius: '50%',
        width: '80px',
        height: '80px',
        lineHeight: '80px',
        margin: 'auto',
        backgroundColor: '#f0f0f0',
        overflow: 'hidden',
        zIndex: 7,
      }}>
        {/* 원형으로 이미지를 꽉 채우기 위해 적절한 스타일을 적용합니다. */}
        <FontAwesomeIcon icon={faUserCircle} size="3x" style={{ width: '80px', height: '80px' }} />
      </div>
    </div>
  );
};

export default MyInfo;

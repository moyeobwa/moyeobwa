import React from 'react';

const Content = ({ meetings }) => {
    const gridStyle = {
      display: 'grid',
      gridTemplateColumns: 'repeat(2, 1fr)', // 항상 2열로 표시합니다.
      gap: '20px',
      justifyContent: 'center', // 그리드 컨테이너를 가운데 정렬합니다.
      padding: '20px',
      maxWidth: '1200px', // 최대 너비 설정
      margin: '0 auto' // 상위 컨테이너에서 중앙 정렬
    };

  const itemStyle = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'flex-start', // 아이템 내부 텍스트를 왼쪽으로 정렬합니다.
    border: '1px solid #ccc',
    borderRadius: '8px', // 모서리를 둥글게 처리합니다.
    width: '500px',
    height: '300px',
    overflow: 'hidden', // 이미지나 내용이 박스 바깥으로 넘치지 않도록 합니다.
    margin: '0 auto'
  };

  const imageStyle = {
    width: '400px',
    height: '200px'
  };

  const titleStyle = {
    fontWeight: 'bold', 
    padding: '10px'
  };

  const descriptionStyle = {
    padding: '10px'
  };

  return (
    <div style={gridStyle}>
      {meetings.map((meeting, index) => (
        <div key={index} style={itemStyle}>
          <img src={meeting.image} alt={meeting.title} style={imageStyle} />
          <div style={titleStyle}>{meeting.title}</div>
          <div style={descriptionStyle}>{meeting.description}</div>
        </div>
      ))}
    </div>
  );
};

export default Content;

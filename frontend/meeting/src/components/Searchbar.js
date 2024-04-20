import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';

const SearchBar = ({ onSearch }) => {
  return (
    <div style={{ position: 'relative', display: 'flex', justifyContent: 'center', alignItems: 'center', width: '100%' }}>
      <input
        type="search"
        placeholder="지금 새로운 모임을 찾아보세요!"
        onChange={onSearch}
        style={{ flex: 1, padding: '10px 40px 10px 10px', width: 'auto' }} // 오른쪽 padding을 증가시켜 아이콘에 공간을 만듭니다.
      />
      <FontAwesomeIcon
        icon={faSearch}
        style={{ position: 'absolute', right: '10px', top: '50%', transform: 'translateY(-50%)', color: '#ccc', pointerEvents: 'none' }} // 아이콘의 스타일을 지정합니다.
      />
    </div>
  );
};

export default SearchBar;

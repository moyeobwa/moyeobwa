import React from 'react';
import SearchBar from './Searchbar';

const Header = ({ projectName }) => {
  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', padding: '10px', borderBottom: '1px solid #ccc' }}>
      <div style={{ width: '25%', textAlign: 'left' }}>
        <h1>{projectName}</h1>
      </div>
      <div style={{ margin: '30px', width: '70%', textAlign: 'center' }}>
        <SearchBar onSearch={() => {}} />
      </div>
      <div style={{ width: '33%' }}></div> {/* This div acts as a spacer */}
    </div>
  );
};

export default Header;

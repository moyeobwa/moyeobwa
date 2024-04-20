import React from 'react';
import Header from './Header';
import Sidebar from './Sidebar';
import Content from './Content';

// Mock data
const userInfo = 'Your Name';
const groups = [{ name: 'Group 1' }, { name: 'Group 2' }]; // Replace with actual group data
const meetings = [
  { title: 'Meeting 1', description: 'Description 1' },
  { title: 'Meeting 2', description: 'Description 2' },
  // Add more meetings as needed
];

const Dashboard = () => {
  return (
    <div>
      <Header projectName="Project_Name" />
      <div style={{ display: 'flex' }}>
        <Sidebar userInfo={userInfo} groups={groups} />
        <Content meetings={meetings} />
      </div>
    </div>
  );
};

export default Dashboard;
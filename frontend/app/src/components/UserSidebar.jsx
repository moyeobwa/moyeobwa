import { useNavigate } from "react-router-dom";
import { useState } from 'react';
import Profile from './Profile';
import MyGatheringList from './MyGatheringList';
import Friend from './Friend';
import Button from './Button';
import './UserSidebar.css';

const UserSidebar = () => {
  const nav = useNavigate();

  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const onClickCreate = () => {
      nav("/gathering/new", { replace: true });
  }

  return (
    <div className="sidebar">
      <Profile />
      <MyGatheringList />
      <Button text={"친구 목록"} onClick={openModal}/>
      <Button text={"그룹 만들기"} onClick={onClickCreate}/>
      <Friend isOpen={isModalOpen} onRequestClose={closeModal} />
    </div>
  );  ``
}

export default UserSidebar;
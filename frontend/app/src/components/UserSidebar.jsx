import { useNavigate } from "react-router-dom";
import Profile from './Profile';
import MyGatheringList from './MyGatheringList';
import Button from './Button';
import './UserSidebar.css';

const UserSidebar = () => {
  const nav = useNavigate();

  const onClickCreate = () => {
      nav("/gathering/new", { replace: true });
  }

  return (
    <div className="sidebar">
      <Profile />
      <MyGatheringList />
      <Button text={"그룹 만들기"} onClick={onClickCreate}/>
    </div>
  );  ``
}

export default UserSidebar;
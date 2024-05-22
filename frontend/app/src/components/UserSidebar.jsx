import Profile from './Profile';
import MyGatheringList from './MyGatheringList';
import Button from './Button';
import './UserSidebar.css';

const UserSidebar = () => {
  return (
    <div className="sidebar">
      <Profile />
      <MyGatheringList />
      <Button text={"그룹 만들기"}/>
    </div>
  );
}

export default UserSidebar;
import './Profile.css';
import profile from "./../assets/profile.png"

const Profile = () => {
  return (
    <div className="profile">
      <img src={profile} alt="프로필 이미지" />
      <div className="info">
        <div className="nickname">윤대현</div>
        <div className="email">eogus0512@gmail.com</div>
      </div>
    </div>
  );
}

export default Profile;
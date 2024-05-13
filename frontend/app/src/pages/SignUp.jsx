import './SignUp.css'
import { useState } from "react"
import Button from '../components/Button';

const SignUp = () => {
    const [nickName, setNickName] = useState("");
    const [profile, setProfile] = useState(null);

    const onChangeNickName = (e) => {
        setNickName(e.target.value);
    }
    const onChangeProfile = (e) => {
        setProfile(e.target.files[0]);
    }


    return (  
        <div className="SignUp">
            <section className="input_section">
                <h1>Sign in</h1>
                <input
                    name="nickName"
                    onChange={onChangeNickName}
                    value={nickName}
                    placeholder='닉네임을 입력해주세요'
                />
                <input
                    type="file"
                    name="profile"
                    onChange={onChangeProfile}
                    value={profile}
                    placeholder='닉네임을 입력해주세요'

                />
                <Button
                    text={"회원가입"}
                />
            </section>
            <section className="intro_section">
                <h2>모여봐에 <br/>오신 것을<br/> 환영합니다!</h2>
                <p>
                    다른 사람들과 취미를 함께 <br/>공유해보세요!
                </p>
            </section>

        </div>
    );
}
 
export default SignUp;
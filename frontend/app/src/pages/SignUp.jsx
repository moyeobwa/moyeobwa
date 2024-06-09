import './SignUp.css';
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import Button from '../components/Button';

const SignUp = () => {
    const [nickName, setNickName] = useState("");
    const [profile, setProfile] = useState(null);
    const navigate = useNavigate();

    const onChangeNickName = (e) => {
        setNickName(e.target.value);
    }

    const onChangeProfile = (e) => {
        setProfile(e.target.files[0]);
    }

    const onSubmit = async () => {
        const formData = new FormData();
        formData.append('image', profile);

        const requestData = {
            nickname: nickName
        };
        formData.append('request', new Blob([JSON.stringify(requestData)], { type: 'application/json' }));

        try {
            // const response = await axios.post('http://3.36.93.156:8080/api/v1/sign-up', formData, {
            const response = await axios.post('http://localhost:8080/api/v1/sign-up', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                }
            });
            if (response.status === 200) {
                alert("회원가입이 완료되었습니다.");
                navigate('/'); // 회원가입 성공 후 홈으로 이동
            }
        } catch (error) {
            console.error(error);
            alert("회원가입에 실패했습니다.");
        }
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
                />
                <Button
                    text={"회원가입"}
                    onClick={onSubmit}
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

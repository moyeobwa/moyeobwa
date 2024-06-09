import axios from 'axios';

const LoginResponse = () => {
  const loginResponse = async () => {
    try {
      const response = await axios.get('http://3.36.93.156:8080/api/v1/tokens');
      if (response.status === 200) {
        console.log(response.data);
      }
    } catch (error) {
      console.error(error);
      alert("토큰을 불러올 수 없습니다.");
    }
  }

  return (
    <button onClick={loginResponse}>토큰 가져오기</button>
  );

  
};

export default LoginResponse;

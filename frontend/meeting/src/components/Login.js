import React from 'react';

class LoginScreen extends React.Component {
  handleLoginClick = () => {
    console.log('로그인 버튼이 클릭됨')
  }
  

  handleGoogleLogin = () => {
    const clientId = process.env.REACT_APP_GOOGLE_AUTH_CLIENT_ID;
    const redirectUri = process.env.REACT_APP_GOOGLE_AUTH_REDIRECT_URI;
    const scope = 'email profile';
    const authUrl = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code&scope=${scope}&access_type=offline`;
    window.location.href = authUrl;
  }

  render() {
    const {onLogin} = this.props;
    const buttonStyle = {
      background: `url('/google_login_button.jpg') no-repeat center center`,
      backgroundSize: '100% 100%',
      width: '250px', 
      height: '50px', 
      border: 'none',
      cursor: 'pointer',
    };

    const textStyle = {
      paddingTop: '50px',
      paddingBottom: '50px'
    }


    return (
      <div style={{ width: '100%', height: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', backgroundColor: '#ffffff' }}>
        <h1 style={textStyle}>안녕하세요, OOO입니다.</h1>
        <h2 style = {textStyle}>OO을 이용해 모임을 관리해보세요!</h2>
        <button style={buttonStyle} onClick={this.handleGoogleLogin}></button>
      </div>
    );
  }
}

export default LoginScreen;

import React from 'react';

class LoginScreen extends React.Component {
  handleLoginClick = () => {
    console.log('로그인 버튼이 클릭됨');
  }

  handleGoogleLogin = () => {
    // Google OAuth 페이지로 리다이렉트
    window.location.href = 'http://ec2-13-125-224-63.ap-northeast-2.compute.amazonaws.com:8080/oauth2/authorization/google';
  }

  render() {
    const { onLogin } = this.props;
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
        <h2 style={textStyle}>OO을 이용해 모임을 관리해보세요!</h2>
        <button style={buttonStyle} onClick={this.handleGoogleLogin}></button>
      </div>
    );
  }
}

export default LoginScreen;

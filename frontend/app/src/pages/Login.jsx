import './Login.css'
import google from "./../assets/google.png"

const Login = () => {
    const apiUrl = import.meta.env.VITE_API_BASE_URL;

    return (  
        <div className="Login">
            <section className="login_section">
                <h1>Login</h1>
                <a href={`${apiUrl}/api/oauth2/authorize/google`}>
                    <img className="google" src={google} alt="Google" />
                </a>
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

export default Login;
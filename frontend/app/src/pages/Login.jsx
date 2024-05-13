import './Login.css'
import { useState } from "react"
import google from "./../assets/google.png"
import { Link } from 'react-router-dom'

const Login = () => {
    return (  
        <div className="Login">
            <section className="login_section">
                <h1>Login</h1>
                <Link to={'/'}>
                    <img className="google" src={google}/>
                </Link>
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
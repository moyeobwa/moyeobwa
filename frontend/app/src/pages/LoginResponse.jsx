import React, { useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import "./LoginResponse.css"

const LoginResponse = () => {
    const navigate = useNavigate();
    const apiUrl = import.meta.env.VITE_API_BASE_URL;

    useEffect(() => {
        const fetchToken = async () => {
            try {
                const response = await axios.get(`${apiUrl}/api/v1/tokens`, {
                    withCredentials: true
                });
                if (response.status === 200) {
                    localStorage.setItem('token', response.data);
                    try {
                        const response = await axios.get(`${apiUrl}/api/v1/login/role`, {
                            headers: {
                                'Authorization': `Bearer ${localStorage.getItem('token')}`
                            }
                        });
                        if (response.status === 200) {
                            const role = response.data;
                            console.log("role : ", role);
                            if (role === 'GUEST') {
                                navigate('/sign-up');
                            } else {
                                navigate('/');
                            }
                        }
                    } catch (error) {
                        console.error(error);
                    }
                }
            } catch (error) {
                console.error(error);
                alert("토큰을 불러올 수 없습니다.");
            }
        }

        fetchToken();
    }, [navigate]);

    return (
        <div class="loader-wrapper">
            <div class="loader"></div>
        </div>
    );
}

export default LoginResponse;

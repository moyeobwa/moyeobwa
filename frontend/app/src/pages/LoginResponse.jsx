import { useEffect, useState } from "react";
import Cookies from 'universal-cookie';

const LoginResponse = () => {
    const [cookieValue, setCookieValue] = useState('');
    const cookies = new Cookies();

    useEffect(() => {
        const token = cookies.get('Authorization-refresh');
        if (token) {
            setCookieValue(token);
        }
    }, [cookies]);

    return (  
        <div className="LoginResponse">
            {/* Token : {token} */}
        </div>
    );
}
 
export default LoginResponse;
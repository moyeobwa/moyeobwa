import { useEffect, useState } from "react";
import Cookies from 'js-cookie';

const LoginResponse = () => {
  const getCookie = () => {
    const token = Cookies.get('Authorization-refresh');
    if (token) {
      alert(token);
    } else {
      alert('No token found');
    }
  };

  return (
    <div>
      <button onClick={getCookie}>Get Cookie</button>
    </div>
  );
}
 
export default LoginResponse;
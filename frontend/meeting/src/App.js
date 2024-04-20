import React, { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginScreen from './components/Login'; // 경로 확인
import Dashboard from './components/Dashboard'; // 경로 확인

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const handleLogin = (credentials) => {
    // 로그인 로직을 처리합니다. 성공하면 상태를 업데이트합니다.
    // 예를 들어 API를 호출하고 그 결과에 따라 로그인 상태를 결정할 수 있습니다.
    setIsLoggedIn(true);
  };

  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={isLoggedIn ? <Navigate replace to="/dashboard" /> : <LoginScreen onLogin={handleLogin} />}
        />
        <Route
          path="/dashboard"
          element={isLoggedIn ? <Dashboard /> : <Navigate replace to="/" />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

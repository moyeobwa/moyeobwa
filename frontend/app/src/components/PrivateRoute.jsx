import { Navigate } from 'react-router-dom';

const PrivateRoute = ({ children }) => {
    const isAuthenticated = () => {
        const token = localStorage.getItem('token');
        return token !== null;
    };
    return isAuthenticated() ? children : <Navigate to="/login" />;
};

export default PrivateRoute;

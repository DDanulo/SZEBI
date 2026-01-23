import { Navigate } from 'react-router-dom';
import { useAuth } from './AuthContext.jsx';

const RequireRole = ({ role, children }) => {
    const { user, loading } = useAuth();

    if (loading) {
        return <div>Ładowanie...</div>;
    }


    if (!user) {
        return <Navigate to="/login" replace />;
    }

    // zalogowany, ale zła rola → 403 / home
    if (user.role !== role) {
        return <Navigate to="/" replace />;
    }

    return children;
};

export default RequireRole;

import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './services/auth';
import AuthCallback from './services/authCallback';
import Login from './pages/Login';
import Register from './pages/Register';
import Main from './pages/Main';

const RootRedirect = () => {
  const { authenticated, loading } = useAuth();
  if (loading) return null;

  return authenticated
    ? <Navigate to="/main" replace />
    : <Navigate to="/register" replace />;
};

const PrivateRoute = ({ children }) => {
  const { authenticated, loading } = useAuth();
  if (loading) return null;

  return authenticated ? children : <Navigate to="/register" replace />;
};


function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<RootRedirect />} />

          {/*public routes */}
          <Route path="/auth/callback" element={<AuthCallback />} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          
          {/*private routes */}
          <Route path="/main" element={<PrivateRoute><Main /></PrivateRoute>} />

          {/*fallback route */}
          <Route path="*" element={<Navigate to="/" />} />

        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
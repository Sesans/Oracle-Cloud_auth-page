import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './services/auth';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';

const RedirectRoute = () => {
  const { authenticated, loading } = useAuth();
  
  if (loading) return <div>Carregando...</div>;
  return authenticated ? <Navigate to="/dashboard" /> : <Navigate to="/register" />;
};


function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<RedirectRoute />} />

          {/*public routes */}
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />

          {/*private routes */}
          <Route path="/dashboard" element={<RedirectRoute />} />
          
          {/*fallback route */}
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
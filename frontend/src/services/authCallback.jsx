import { useEffect, useRef } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from './auth';

export default function AuthCallback() {
  const [params] = useSearchParams();
  const navigate = useNavigate();
  const { login } = useAuth();
  const processed = useRef(false);

  useEffect(() => {
    if (processed.current) return;

    const token = params.get('token');

    if (!token) {
      navigate('/login', { replace: true });
      return;
    }

    processed.current = true;

    login(
      { provider: 'google' },
      token
    );

    navigate('/main', { replace: true });
  }, [params, navigate, login]);

  return <h2>Finalizing authentication…</h2>;
}

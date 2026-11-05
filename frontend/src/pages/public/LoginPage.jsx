import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { HeartPulse, Eye, EyeOff } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';

export default function LoginPage() {
  const [email, setEmail] = useState('admin@liferoute.ai');
  const [password, setPassword] = useState('Admin1234');
  const [showPassword, setShowPassword] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');
  const [loading, setLoading] = useState(false);

  const { login, user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const msg = sessionStorage.getItem('auth_message');
    if (msg) {
      setErrorMsg(msg);
      sessionStorage.removeItem('auth_message');
    }
  }, []);

  useEffect(() => {
    if (user) {
      if (user.role === 'ADMIN') navigate('/admin');
      else if (user.role === 'HOSPITAL') navigate('/hospital');
      else navigate('/me');
    }
  }, [user, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    setLoading(true);
    try {
      const profile = await login(email, password);
      if (profile.role === 'ADMIN') navigate('/admin');
      else if (profile.role === 'HOSPITAL') navigate('/hospital');
      else navigate('/me');
    } catch (err) {
      const msg = err.response?.data?.message || 'Invalid email or password.';
      setErrorMsg(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '16px' }}>
      <div style={{ background: '#FFFFFF', borderRadius: 'var(--radius-card)', border: '1px solid var(--color-border)', boxShadow: 'var(--shadow-modal)', width: '100%', maxWidth: '420px', padding: '36px' }}>
        <div style={{ textAlign: 'center', marginBottom: '24px' }}>
          <HeartPulse size={36} color="var(--color-accent)" style={{ marginBottom: '8px' }} />
          <h2>Life Root AI</h2>
          <p className="text-small" style={{ color: 'var(--color-text-muted)' }}>Sign in with your institutional credentials</p>
        </div>

        {errorMsg && (
          <div style={{ background: '#FEE2E2', color: 'var(--color-danger)', padding: '12px', borderRadius: '8px', fontSize: '14px', marginBottom: '16px', borderLeft: '4px solid var(--color-danger)' }}>
            {errorMsg}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <Input
            label="Email Address"
            type="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />

          <div style={{ position: 'relative' }}>
            <Input
              label="Password"
              type={showPassword ? 'text' : 'password'}
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--color-text-muted)', float: 'right', marginTop: '-46px', marginRight: '12px', position: 'relative', zIndex: 2 }}
            >
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>

          <Button type="submit" variant="primary" size="large" loading={loading} style={{ width: '100%', marginTop: '8px' }}>
            Sign in
          </Button>
        </form>

        <div style={{ marginTop: '24px', textAlign: 'center', display: 'flex', flexDirection: 'column', gap: '10px', fontSize: '14px' }}>
          <Link to="/hospital/activate" style={{ color: 'var(--color-primary)', fontWeight: 600, textDecoration: 'none' }}>
            First time hospital? Activate with security code
          </Link>
          <Link to="/" style={{ color: 'var(--color-text-muted)', textDecoration: 'none' }}>
            Back to Home
          </Link>
        </div>
      </div>
    </div>
  );
}


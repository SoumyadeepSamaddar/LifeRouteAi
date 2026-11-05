import React from 'react';
import { useNavigate } from 'react-router-dom';
import { ShieldAlert } from 'lucide-react';
import { Button } from '../../components/ui/Button';

export default function ErrorPage({ code = 404, title = 'Page Not Found', message = 'The page you requested does not exist.' }) {
  const navigate = useNavigate();
  return (
    <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', textAlign: 'center', padding: '24px' }}>
      <div style={{ maxWidth: '420px' }}>
        <ShieldAlert size={56} color="var(--color-danger)" style={{ marginBottom: '16px' }} />
        <h1>{code} — {title}</h1>
        <p style={{ color: 'var(--color-text-muted)', margin: '12px 0 24px' }}>{message}</p>
        <Button variant="primary" onClick={() => navigate('/login')}>Go to Dashboard</Button>
      </div>
    </div>
  );
}


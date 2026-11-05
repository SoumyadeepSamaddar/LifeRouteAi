import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { ShieldCheck, CheckCircle2 } from 'lucide-react';
import axiosClient from '../../api/axiosClient';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';

export default function HospitalActivationPage() {
  const [step, setStep] = useState(1);
  const [code, setCode] = useState('');
  const [verifiedInfo, setVerifiedInfo] = useState(null);
  const [form, setForm] = useState({ adminName: '', email: '', phone: '', password: '' });
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleVerifyCode = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await axiosClient.post('/hospital/activate/verify', { code: code.trim().toUpperCase() });
      setVerifiedInfo(res.data);
      setStep(2);
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid, expired, or revoked security code.');
    } finally {
      setLoading(false);
    }
  };

  const handleActivate = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await axiosClient.post('/hospital/activate', {
        code: code.trim().toUpperCase(),
        ...form,
      });
      setSuccessMsg(res.data.message || 'Hospital activated! Redirecting to login...');
      setTimeout(() => navigate('/login'), 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Activation failed. Check password requirements.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '16px' }}>
      <div style={{ background: '#FFFFFF', borderRadius: 'var(--radius-card)', border: '1px solid var(--color-border)', boxShadow: 'var(--shadow-modal)', width: '100%', maxWidth: '480px', padding: '36px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '16px' }}>
          <ShieldCheck size={28} color="var(--color-primary)" />
          <h2>Hospital Onboarding</h2>
        </div>

        {/* Stepper Header */}
        <div style={{ display: 'flex', gap: '8px', marginBottom: '24px', fontSize: '13px', fontWeight: 600 }}>
          <span style={{ color: step === 1 ? 'var(--color-primary)' : 'var(--color-success)' }}>
            1. Verify Security Code
          </span>
          <span>→</span>
          <span style={{ color: step === 2 ? 'var(--color-primary)' : 'var(--color-text-muted)' }}>
            2. Create Hospital Admin
          </span>
        </div>

        {error && (
          <div style={{ background: '#FEE2E2', color: 'var(--color-danger)', padding: '12px', borderRadius: '8px', fontSize: '14px', marginBottom: '16px' }}>
            {error}
          </div>
        )}

        {successMsg && (
          <div style={{ background: '#DCFCE7', color: '#166534', padding: '12px', borderRadius: '8px', fontSize: '14px', marginBottom: '16px' }}>
            {successMsg}
          </div>
        )}

        {step === 1 ? (
          <form onSubmit={handleVerifyCode}>
            <Input
              label="One-Time Security Code"
              placeholder="LR-XXXX-XXXX"
              required
              helperText="Your platform administrator generated this code for your hospital."
              value={code}
              onChange={(e) => setCode(e.target.value.toUpperCase())}
            />
            <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '24px' }}>
              <Link to="/login"><Button variant="secondary">Back to Login</Button></Link>
              <Button type="submit" variant="primary" loading={loading}>Verify Code</Button>
            </div>
          </form>
        ) : (
          <form onSubmit={handleActivate}>
            <div style={{ background: 'var(--color-primary-light)', padding: '12px', borderRadius: '8px', marginBottom: '16px', fontSize: '14px', color: 'var(--color-primary-dark)' }}>
              <CheckCircle2 size={16} style={{ verticalAlign: 'middle', marginRight: '6px' }} />
              Verified: <strong>{verifiedInfo?.hospitalName}</strong> ({verifiedInfo?.city})
            </div>

            <Input
              label="Administrator Full Name"
              required
              value={form.adminName}
              onChange={(e) => setForm({ ...form, adminName: e.target.value })}
            />
            <Input
              label="Official Email"
              type="email"
              required
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
            />
            <Input
              label="Phone Number"
              required
              value={form.phone}
              onChange={(e) => setForm({ ...form, phone: e.target.value })}
            />
            <Input
              label="Root Password"
              type="password"
              required
              helperText="Min 8 chars, 1 uppercase, 1 lowercase, 1 digit"
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
            />

            <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '24px' }}>
              <Button variant="secondary" onClick={() => setStep(1)}>Back</Button>
              <Button type="submit" variant="primary" loading={loading}>Activate Hospital</Button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
}


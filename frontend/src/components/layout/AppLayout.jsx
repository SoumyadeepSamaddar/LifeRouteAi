import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import {
  HeartPulse, LayoutDashboard, Building2, KeyRound, Settings,
  ShieldAlert, Users, Award, LogOut, RefreshCw
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { Button } from '../ui/Button';

export const AppLayout = ({ children }) => {
  const { user, logout, pingSession } = useAuth();
  const location = useLocation();
  const [pingStatus, setPingStatus] = useState('');

  const handlePing = async () => {
    try {
      await pingSession();
      setPingStatus('Session extended (30m)');
      setTimeout(() => setPingStatus(''), 3000);
    } catch {
      setPingStatus('Failed to ping');
    }
  };

  const adminLinks = [
    { to: '/admin', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/admin/hospitals', label: 'Hospitals', icon: Building2 },
    { to: '/admin/security-codes', label: 'Security Codes', icon: KeyRound },
    { to: '/admin/staff', label: 'Hospital Staff', icon: Users },
    { to: '/admin/civic-points', label: 'Civic Points (Alg 4)', icon: Award },
    { to: '/admin/settings', label: 'Algorithm Settings', icon: Settings },
    { to: '/admin/audit', label: 'Audit Log', icon: ShieldAlert },
  ];

  const hospitalLinks = [
    { to: '/hospital', label: 'Hospital Dashboard', icon: LayoutDashboard },
    { to: '/hospital/staff', label: 'Manage Staff', icon: Users },
  ];

  const portalLinks = [
    { to: '/me', label: 'My Portal & Civic Score', icon: Award },
  ];

  const navItems =
    user?.role === 'ADMIN' ? adminLinks :
    user?.role === 'HOSPITAL' ? hospitalLinks : portalLinks;

  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      {/* 260px Deep Navy Left Sidebar (Section 7.7) */}
      <aside
        style={{
          width: '260px',
          background: 'var(--color-secondary)',
          color: '#FFFFFF',
          display: 'flex',
          flexDirection: 'column',
          flexShrink: 0,
        }}
      >
        <div style={{ height: '64px', display: 'flex', alignItems: 'center', gap: '10px', padding: '0 20px', borderBottom: '1px solid rgba(255,255,255,0.1)' }}>
          <HeartPulse size={26} color="var(--color-accent)" />
          <span style={{ fontSize: '18px', fontWeight: 700, letterSpacing: '-0.3px' }}>Life Root AI</span>
        </div>

        <nav style={{ padding: '16px 12px', flex: 1, display: 'flex', flexDirection: 'column', gap: '4px' }}>
          {navItems.map((item) => {
            const Icon = item.icon;
            const active = location.pathname === item.to;
            return (
              <Link
                key={item.to}
                to={item.to}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: '12px',
                  padding: '10px 12px',
                  borderRadius: '8px',
                  textDecoration: 'none',
                  color: '#FFFFFF',
                  fontSize: '14px',
                  fontWeight: active ? 600 : 400,
                  background: active ? 'var(--color-primary)' : 'transparent',
                }}
              >
                <Icon size={20} />
                {item.label}
              </Link>
            );
          })}
        </nav>

        <div style={{ padding: '16px', borderTop: '1px solid rgba(255,255,255,0.1)', fontSize: '12px', color: '#94A3B8' }}>
          <div>Signed in as:</div>
          <div style={{ color: '#FFFFFF', fontWeight: 600, marginTop: '2px' }}>{user?.name}</div>
          <div>Role: {user?.role} {user?.staffRole ? `(${user.staffRole})` : ''}</div>
        </div>
      </aside>

      {/* Main Content Area */}
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        {/* 64px Top Bar */}
        <header
          style={{
            height: '64px',
            background: 'var(--color-surface)',
            borderBottom: '1px solid var(--color-border)',
            padding: '0 24px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <span className="text-small" style={{ color: 'var(--color-text-muted)' }}>
              Biomedical Organ Allocation & Green Corridor Platform
            </span>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            {pingStatus && <span className="text-caption" style={{ color: 'var(--color-success)' }}>{pingStatus}</span>}
            <Button variant="ghost" size="small" icon={RefreshCw} onClick={handlePing}>
              Stay signed in
            </Button>
            <Button variant="secondary" size="small" icon={LogOut} onClick={logout}>
              Logout
            </Button>
          </div>
        </header>

        <main style={{ padding: '24px', maxWidth: '1280px', width: '100%', margin: '0 auto', flex: 1 }}>
          {children}
        </main>
      </div>
    </div>
  );
};


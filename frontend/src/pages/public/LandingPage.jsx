import React from 'react';
import { useNavigate } from 'react-router-dom';
import { HeartPulse, Activity, MapPin, ShieldCheck, BellRing, Award, ArrowRight } from 'lucide-react';
import { Button } from '../../components/ui/Button';
import { Card } from '../../components/ui/Card';

export default function LandingPage() {
  const navigate = useNavigate();

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      {/* 64px Public Navbar */}
      <header style={{ height: '64px', background: '#FFFFFF', borderBottom: '1px solid var(--color-border)', padding: '0 32px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <HeartPulse size={28} color="var(--color-accent)" />
          <span style={{ fontSize: '20px', fontWeight: 700, color: 'var(--color-secondary)' }}>Life Root AI</span>
        </div>
        <div style={{ display: 'flex', gap: '12px' }}>
          <Button variant="secondary" onClick={() => navigate('/hospital/activate')}>Activate Hospital</Button>
          <Button variant="primary" onClick={() => navigate('/login')}>Login</Button>
        </div>
      </header>

      {/* Hero Section */}
      <section style={{ background: 'var(--color-secondary)', color: '#FFFFFF', padding: '72px 24px', textAlign: 'center' }}>
        <div style={{ maxWidth: '800px', margin: '0 auto' }}>
          <h1 style={{ color: '#FFFFFF', fontSize: '40px', lineHeight: '48px', marginBottom: '16px' }}>
            Right organ. Right patient. Right on time.
          </h1>
          <p style={{ color: '#CBD5E1', fontSize: '18px', marginBottom: '32px' }}>
            AI-assisted donor-recipient cross-matching, cold ischemia viability verification, and multi-mode emergency green corridor routing.
          </p>
          <div style={{ display: 'flex', gap: '16px', justifyContent: 'center', flexWrap: 'wrap' }}>
            <Button variant="primary" size="large" icon={ArrowRight} onClick={() => navigate('/login')}>
              Sign in to Platform
            </Button>
            <Button variant="secondary" size="large" onClick={() => navigate('/hospital/activate')}>
              Hospital Security Code Activation
            </Button>
          </div>
        </div>
      </section>

      {/* Key Features (6 Cards from P1 Spec) */}
      <section style={{ maxWidth: '1200px', margin: '48px auto', padding: '0 24px', flex: 1 }}>
        <h2 style={{ textAlign: 'center', marginBottom: '32px' }}>Core Platform Capabilities</h2>
        <div className="grid-3">
          <Card title="1. AI Compatibility Scoring">
            <p className="text-small" style={{ color: 'var(--color-text-muted)' }}>
              Evaluates blood group, HLA, clinical health, and wait-time urgency with transparent weightings.
            </p>
          </Card>
          <Card title="2. Cold Ischemia Viability">
            <p className="text-small" style={{ color: 'var(--color-text-muted)' }}>
              Ensures transit ETA plus surgical handover buffer stays strictly within organ shelf-life limits.
            </p>
          </Card>
          <Card title="3. Cryptographic Onboarding">
            <p className="text-small" style={{ color: 'var(--color-text-muted)' }}>
              One-time BCrypt-hashed activation codes (LR-XXXX-XXXX) for verified transplant centers.
            </p>
          </Card>
          <Card title="4. Multi-Mode Transit">
            <p className="text-small" style={{ color: 'var(--color-text-muted)' }}>
              Compares road, air ambulance, and express rail corridors with real-time congestion penalties.
            </p>
          </Card>
          <Card title="5. Civic Priority Engine">
            <p className="text-small" style={{ color: 'var(--color-text-muted)' }}>
              Algorithm 4 tracks donor pledges and awareness camp participation with transparent tier multipliers.
            </p>
          </Card>
          <Card title="6. Immutable Audit Trail">
            <p className="text-small" style={{ color: 'var(--color-text-muted)' }}>
              Every login, weight adjustment, and clinical status change is permanently logged for governance.
            </p>
          </Card>
        </div>
      </section>

      {/* Medical Disclaimer Footer (Section 8.1 P1) */}
      <footer style={{ background: '#FFFFFF', borderTop: '1px solid var(--color-border)', padding: '24px', textAlign: 'center' }}>
        <p className="text-small" style={{ color: 'var(--color-text-muted)' }}>
          <strong>Medical Disclaimer:</strong> Life Root AI supports doctors as a clinical decision-support tool. Final medical decisions are always made by the transplant team.
        </p>
      </footer>
    </div>
  );
}


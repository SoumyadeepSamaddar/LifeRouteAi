import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Building2, KeyRound, ShieldCheck, Sliders, Plus, Award } from 'lucide-react';
import axiosClient from '../../api/axiosClient';
import { StatCard, Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';

export default function AdminDashboard() {
  const [hospitals, setHospitals] = useState([]);
  const [codes, setCodes] = useState([]);
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    Promise.all([
      axiosClient.get('/admin/hospitals').catch(() => ({ data: [] })),
      axiosClient.get('/admin/security-codes').catch(() => ({ data: [] })),
      axiosClient.get('/admin/audit-logs').catch(() => ({ data: [] })),
    ]).then(([hRes, cRes, lRes]) => {
      setHospitals(hRes.data || []);
      setCodes(cRes.data || []);
      setLogs((lRes.data || []).slice(0, 6));
      setLoading(false);
    });
  }, []);

  const activeHospitals = hospitals.filter((h) => h.status === 'ACTIVE').length;
  const pendingHospitals = hospitals.filter((h) => h.status === 'PENDING_ACTIVATION').length;
  const activeCodes = codes.filter((c) => c.status === 'ACTIVE').length;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Admin Command Center</h1>
          <p className="page-subtitle">Platform governance, hospital onboarding, and AI matching parameter control</p>
        </div>
        <div style={{ display: 'flex', gap: '12px' }}>
          <Button variant="secondary" icon={KeyRound} onClick={() => navigate('/admin/security-codes')}>
            Generate Security Code
          </Button>
          <Button variant="primary" icon={Plus} onClick={() => navigate('/admin/hospitals')}>
            Add Hospital
          </Button>
        </div>
      </div>

      {/* Stat Cards (Section 8.2 A1) */}
      <div className="grid-stats">
        <StatCard label="Total Hospitals" value={loading ? '...' : hospitals.length} trend={`${activeHospitals} Active`} icon={Building2} />
        <StatCard label="Pending Activation" value={loading ? '...' : pendingHospitals} trend="Awaiting code verification" icon={Sliders} accentColor="var(--color-warning)" />
        <StatCard label="Active Security Codes" value={loading ? '...' : activeCodes} trend="72h validity window" icon={KeyRound} accentColor="var(--color-info)" />
        <StatCard label="Audit Events Logged" value={loading ? '...' : logs.length} trend="Immutable MySQL trail" icon={ShieldCheck} accentColor="var(--color-success)" />
      </div>

      <div className="grid-2">
        <Card title="Registered Hospitals" subtitle="Latest transplant centers onboarded">
          {hospitals.length === 0 ? (
            <p className="text-small" style={{ color: 'var(--color-text-muted)' }}>No hospitals registered yet.</p>
          ) : (
            <table className="ui-table">
              <thead>
                <tr>
                  <th>Hospital</th>
                  <th>City</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {hospitals.slice(0, 5).map((h) => (
                  <tr key={h.id}>
                    <td style={{ fontWeight: 600 }}>{h.name}</td>
                    <td>{h.city}</td>
                    <td><Badge status={h.status} /></td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </Card>

        <Card title="Recent Security & Governance Activity" subtitle="Latest entries from audit_logs">
          {logs.length === 0 ? (
            <p className="text-small" style={{ color: 'var(--color-text-muted)' }}>No activity recorded yet.</p>
          ) : (
            <table className="ui-table">
              <thead>
                <tr>
                  <th>Action</th>
                  <th>User</th>
                  <th>Target</th>
                </tr>
              </thead>
              <tbody>
                {logs.map((log) => (
                  <tr key={log.id}>
                    <td><Badge status={log.actionType} /></td>
                    <td>{log.userEmail}</td>
                    <td className="text-caption">{log.targetObject}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </Card>
      </div>
    </div>
  );
}


import React, { useEffect, useState } from 'react';
import { Award } from 'lucide-react';
import axiosClient from '../../api/axiosClient';
import { Card, StatCard } from '../../components/ui/Card';
import { useAuth } from '../../context/AuthContext';

export default function UserPortalPage() {
  const { user } = useAuth();
  const [civic, setCivic] = useState(null);

  useEffect(() => {
    axiosClient.get('/me/civic-points').then((res) => setCivic(res.data)).catch(() => {});
  }, []);

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Welcome, {user?.name}</h1>
          <p className="page-subtitle">Patient / Donor Portal & Civic Priority Score</p>
        </div>
      </div>

      <div className="grid-stats">
        <StatCard label="Civic Score" value={civic?.totalPoints ?? 0} trend={civic?.civicTier || 'BRONZE_MEMBER'} icon={Award} />
        <StatCard label="Priority Bonus" value={`+${((civic?.priorityBonusMultiplier || 0) * 100).toFixed(0)}%`} trend="Algorithm 4 Tie-Breaker" />
      </div>

      <Card title="My Civic Contribution History">
        <p className="text-small" style={{ color: 'var(--color-text-muted)' }}>
          Points are awarded by verified hospitals and awareness camps.
        </p>
      </Card>
    </div>
  );
}


import React, { useState } from 'react';
import { Award, Search } from 'lucide-react';
import axiosClient from '../../api/axiosClient';
import { Card, StatCard } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input, Select } from '../../components/ui/Input';
import { Badge } from '../../components/ui/Badge';

export default function CivicPointsPage() {
  const [lookupUserId, setLookupUserId] = useState('1');
  const [summary, setSummary] = useState(null);
  const [awardForm, setAwardForm] = useState({ userId: '1', activityType: 'DONATION_PLEDGE', remarks: '' });
  const [message, setMessage] = useState('');

  const handleLookup = async (e) => {
    if (e) e.preventDefault();
    try {
      const res = await axiosClient.get(`/admin/civic-points/user/${lookupUserId}`);
      setSummary(res.data);
    } catch (err) {
      setMessage(err.response?.data?.message || 'User not found');
    }
  };

  const handleAward = async (e) => {
    e.preventDefault();
    setMessage('');
    try {
      await axiosClient.post('/admin/civic-points', {
        userId: Number(awardForm.userId),
        activityType: awardForm.activityType,
        remarks: awardForm.remarks,
      });
      setMessage('Civic points awarded successfully!');
      setLookupUserId(awardForm.userId);
      const res = await axiosClient.get(`/admin/civic-points/user/${awardForm.userId}`);
      setSummary(res.data);
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to award points');
    }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Civic Points Engine — Algorithm 4</h1>
          <p className="page-subtitle">Award donor awareness & pledge points and calculate tie-breaker priority multipliers</p>
        </div>
      </div>

      {message && (
        <div style={{ background: '#DCFCE7', color: '#166534', padding: '12px', borderRadius: '8px', marginBottom: '16px' }}>
          {message}
        </div>
      )}

      <div className="grid-2">
        <Card title="Award Civic Activity Points">
          <form onSubmit={handleAward}>
            <Input
              label="Target User ID"
              type="number"
              required
              value={awardForm.userId}
              onChange={(e) => setAwardForm({ ...awardForm, userId: e.target.value })}
            />
            <Select
              label="Civic Activity Type"
              value={awardForm.activityType}
              onChange={(e) => setAwardForm({ ...awardForm, activityType: e.target.value })}
              options={[
                { value: 'REGISTRATION', label: 'Registration (+50 pts)' },
                { value: 'CAMP_ATTENDANCE', label: 'Awareness Camp Attendance (+100 pts)' },
                { value: 'REFERRAL', label: 'Verified Referral (+150 pts)' },
                { value: 'DONATION_PLEDGE', label: 'Organ Donation Pledge (+500 pts)' },
              ]}
            />
            <Input
              label="Verification Remarks"
              placeholder="Certificate ID or event reference"
              value={awardForm.remarks}
              onChange={(e) => setAwardForm({ ...awardForm, remarks: e.target.value })}
            />
            <Button type="submit" variant="primary" icon={Award}>Award Points</Button>
          </form>
        </Card>

        <Card title="Inspect User Civic Tier & Priority Multiplier">
          <form onSubmit={handleLookup} style={{ display: 'flex', gap: '12px', alignItems: 'flex-end', marginBottom: '20px' }}>
            <Input
              label="User ID"
              type="number"
              style={{ marginBottom: 0, flex: 1 }}
              value={lookupUserId}
              onChange={(e) => setLookupUserId(e.target.value)}
            />
            <Button type="submit" variant="secondary" icon={Search}>Check Summary</Button>
          </form>

          {summary && (
            <div>
              <div className="grid-2" style={{ marginBottom: '16px' }}>
                <StatCard label="Total Civic Points" value={summary.totalPoints} trend={summary.civicTier} />
                <StatCard label="Tie-Breaker Multiplier" value={`+${(summary.priorityBonusMultiplier * 100).toFixed(0)}%`} trend={summary.userEmail} />
              </div>
              <table className="ui-table">
                <thead>
                  <tr>
                    <th>Activity</th>
                    <th>Points</th>
                    <th>Remarks</th>
                  </tr>
                </thead>
                <tbody>
                  {(summary.history || []).map((h) => (
                    <tr key={h.id}>
                      <td><Badge status="ACTIVE">{h.activityType}</Badge></td>
                      <td style={{ fontWeight: 700 }}>+{h.points}</td>
                      <td className="text-small">{h.remarks}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </Card>
      </div>
    </div>
  );
}

import React, { useEffect, useState } from 'react';
import { Save } from 'lucide-react';
import axiosClient from '../../api/axiosClient';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';

export default function SystemSettingsPage() {
  const [settings, setSettings] = useState([]);
  const [activeCategory, setActiveCategory] = useState('MATCHING_WEIGHTS');
  const [editValues, setEditValues] = useState({});
  const [message, setMessage] = useState('');

  const fetchSettings = async () => {
    const res = await axiosClient.get('/admin/settings');
    setSettings(res.data || []);
    const map = {};
    (res.data || []).forEach((s) => { map[s.settingKey] = s.settingValue; });
    setEditValues(map);
  };

  useEffect(() => {
    fetchSettings();
  }, []);

  const handleSaveKey = async (setting) => {
    setMessage('');
    try {
      await axiosClient.put(`/admin/settings/${setting.settingKey}`, {
        settingValue: editValues[setting.settingKey],
        description: setting.description,
      });
      setMessage(`Updated ${setting.settingKey} successfully.`);
      fetchSettings();
    } catch (err) {
      setMessage(err.response?.data?.message || 'Error updating setting');
    }
  };

  const categories = ['MATCHING_WEIGHTS', 'ORGAN_SHELF_LIFE', 'TRANSPORT', 'RETRY_ALERTS'];
  const filtered = settings.filter((s) => s.category === activeCategory);

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Algorithm & System Settings (Page A9)</h1>
          <p className="page-subtitle">Configure AI matching weights (a1–a4), cold ischemia limits, and traffic congestion penalties</p>
        </div>
      </div>

      <div style={{ background: '#FEF3C7', color: '#92400E', padding: '12px 16px', borderRadius: '8px', marginBottom: '20px', fontSize: '14px', borderLeft: '4px solid var(--color-warning)' }}>
        Changes affect all hospitals immediately and are recorded in the immutable audit log. Medical advisor approval recommended.
      </div>

      {message && (
        <div style={{ background: '#DCFCE7', color: '#166534', padding: '12px', borderRadius: '8px', marginBottom: '16px', fontSize: '14px' }}>
          {message}
        </div>
      )}

      {/* Tabs */}
      <div style={{ display: 'flex', gap: '8px', marginBottom: '20px', flexWrap: 'wrap' }}>
        {categories.map((cat) => (
          <Button
            key={cat}
            variant={activeCategory === cat ? 'primary' : 'secondary'}
            size="small"
            onClick={() => setActiveCategory(cat)}
          >
            {cat.replace(/_/g, ' ')}
          </Button>
        ))}
      </div>

      <Card>
        <table className="ui-table">
          <thead>
            <tr>
              <th>Setting Key</th>
              <th>Description</th>
              <th style={{ width: '180px' }}>Value</th>
              <th style={{ width: '120px' }}>Action</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((s) => (
              <tr key={s.settingKey}>
                <td style={{ fontFamily: 'monospace', fontWeight: 600 }}>{s.settingKey}</td>
                <td>{s.description}</td>
                <td>
                  <Input
                    style={{ marginBottom: 0 }}
                    value={editValues[s.settingKey] ?? ''}
                    onChange={(e) => setEditValues({ ...editValues, [s.settingKey]: e.target.value })}
                  />
                </td>
                <td>
                  <Button variant="primary" size="small" icon={Save} onClick={() => handleSaveKey(s)}>
                    Save
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </Card>
    </div>
  );
}


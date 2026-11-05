import React, { useEffect, useState } from 'react';
import axiosClient from '../../api/axiosClient';
import { Badge } from '../../components/ui/Badge';
import { Input } from '../../components/ui/Input';

export default function AuditLogPage() {
  const [logs, setLogs] = useState([]);
  const [filter, setFilter] = useState('');

  useEffect(() => {
    axiosClient.get('/admin/audit-logs').then((res) => setLogs(res.data || []));
  }, []);

  const filtered = logs.filter(
    (l) =>
      (l.userEmail || '').toLowerCase().includes(filter.toLowerCase()) ||
      (l.actionType || '').toLowerCase().includes(filter.toLowerCase()) ||
      (l.targetObject || '').toLowerCase().includes(filter.toLowerCase())
  );

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Immutable Audit Log (Page A10)</h1>
          <p className="page-subtitle">Read-only security and clinical compliance trace across all platform roles</p>
        </div>
      </div>

      <div style={{ maxWidth: '360px', marginBottom: '16px' }}>
        <Input
          placeholder="Filter by user email, action type, or object..."
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
        />
      </div>

      <div className="ui-table-wrapper">
        <table className="ui-table">
          <thead>
            <tr>
              <th>Timestamp</th>
              <th>User Email</th>
              <th>Role</th>
              <th>Action</th>
              <th>Target Object</th>
              <th>Details</th>
              <th>IP</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((log) => (
              <tr key={log.id}>
                <td className="text-caption">{log.createdAt}</td>
                <td style={{ fontWeight: 600 }}>{log.userEmail}</td>
                <td>{log.userRole}</td>
                <td><Badge status={log.actionType} /></td>
                <td style={{ fontFamily: 'monospace', fontSize: '12px' }}>{log.targetObject}</td>
                <td className="text-small">{log.details}</td>
                <td className="text-caption">{log.ipAddress}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}


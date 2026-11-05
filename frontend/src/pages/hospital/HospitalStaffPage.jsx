import React, { useEffect, useState } from 'react';
import { UserPlus } from 'lucide-react';
import axiosClient from '../../api/axiosClient';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input, Select } from '../../components/ui/Input';
import { Badge } from '../../components/ui/Badge';
import { useAuth } from '../../context/AuthContext';

export default function HospitalStaffPage() {
  const { user } = useAuth();
  const [staff, setStaff] = useState([]);
  const [hospitalId, setHospitalId] = useState(user?.hospitalId || 1);
  const [form, setForm] = useState({
    name: '',
    email: '',
    phone: '',
    password: '',
    staffRole: 'COORDINATOR',
  });
  const [msg, setMsg] = useState('');

  const fetchStaff = async (hId) => {
    try {
      const res = await axiosClient.get(`/hospital/staff?hospitalId=${hId}`);
      setStaff(res.data || []);
    } catch {
      setStaff([]);
    }
  };

  useEffect(() => {
    fetchStaff(hospitalId);
  }, [hospitalId]);

  const handleCreate = async (e) => {
    e.preventDefault();
    setMsg('');
    try {
      await axiosClient.post('/hospital/staff', {
        ...form,
        hospitalId: Number(hospitalId),
      });
      setMsg('Staff member onboarded successfully!');
      setForm({ name: '', email: '', phone: '', password: '', staffRole: 'COORDINATOR' });
      fetchStaff(hospitalId);
    } catch (err) {
      setMsg(err.response?.data?.message || 'Error onboarding staff member.');
    }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Hospital Staff Management</h1>
          <p className="page-subtitle">Provision Transplant Coordinators, Surgeons (Doctors), and Dispatch Officers</p>
        </div>
      </div>

      {msg && (
        <div style={{ background: '#CCFBF1', color: '#115E59', padding: '12px', borderRadius: '8px', marginBottom: '16px' }}>
          {msg}
        </div>
      )}

      <div className="grid-2">
        <Card title="Onboard Hospital Staff">
          <form onSubmit={handleCreate}>
            {user?.role === 'ADMIN' && (
              <Input
                label="Hospital ID"
                type="number"
                required
                value={hospitalId}
                onChange={(e) => setHospitalId(e.target.value)}
              />
            )}
            <Input label="Full Name" required value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
            <Input label="Institutional Email" type="email" required value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
            <Input label="Phone Number" required value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
            <Select
              label="Hospital Staff Role (Section 4.1)"
              value={form.staffRole}
              onChange={(e) => setForm({ ...form, staffRole: e.target.value })}
              options={[
                { value: 'COORDINATOR', label: 'Coordinator (Organ Procurement & Queue)' },
                { value: 'DOCTOR', label: 'Doctor / Surgeon (Match Confirmation)' },
                { value: 'DISPATCH', label: 'Dispatch (Green Corridor & Routing)' },
                { value: 'HOSPITAL_ADMIN', label: 'Hospital Admin' },
              ]}
            />
            <Input
              label="Temporary Password"
              type="password"
              required
              helperText="Min 8 chars, uppercase, lowercase, and number"
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
            />
            <Button type="submit" variant="primary" icon={UserPlus}>Create Staff Account</Button>
          </form>
        </Card>

        <Card title={`Active Staff Directory (Hospital #${hospitalId})`}>
          <table className="ui-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Staff Role</th>
              </tr>
            </thead>
            <tbody>
              {staff.map((s) => (
                <tr key={s.id}>
                  <td style={{ fontWeight: 600 }}>{s.name}</td>
                  <td>{s.email}</td>
                  <td><Badge status="MATCHED">{s.staffRole}</Badge></td>
                </tr>
              ))}
            </tbody>
          </table>
        </Card>
      </div>
    </div>
  );
}


import React, { useEffect, useState } from 'react';
import { Plus, KeyRound } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import axiosClient from '../../api/axiosClient';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { Input, Select } from '../../components/ui/Input';
import { Modal } from '../../components/ui/Modal';

const INITIAL_FORM = {
  name: '',
  registrationNo: '',
  hospitalType: 'PRIVATE',
  licensedOrgans: 'HEART,LUNG,LIVER,KIDNEY',
  contactPerson: '',
  phone: '',
  email: '',
  emergencyPhone: '',
  address: '',
  city: 'Kolkata',
  state: 'West Bengal',
  pincode: '',
  latitude: 22.5726,
  longitude: 88.3639,
  hasHelipad: false,
  hasTraumaCenter: true,
  operationTheatersCount: 4,
  nearestAirport: 'CCU Netaji Subhash Chandra Bose Airport',
  nearestRailwayStation: 'Sealdah',
};

export default function HospitalsPage() {
  const [hospitals, setHospitals] = useState([]);
  const [search, setSearch] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [form, setForm] = useState(INITIAL_FORM);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);
  const navigate = useNavigate();

  const fetchHospitals = async () => {
    const res = await axiosClient.get('/admin/hospitals');
    setHospitals(res.data || []);
  };

  useEffect(() => {
    fetchHospitals();
  }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    setError('');
    setSaving(true);
    try {
      await axiosClient.post('/admin/hospitals', {
        ...form,
        latitude: parseFloat(form.latitude),
        longitude: parseFloat(form.longitude),
        operationTheatersCount: parseInt(form.operationTheatersCount, 10),
      });
      setIsModalOpen(false);
      setForm(INITIAL_FORM);
      fetchHospitals();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create hospital.');
    } finally {
      setSaving(false);
    }
  };

  const handleStatusChange = async (id, nextStatus) => {
    await axiosClient.patch(`/admin/hospitals/${id}/status?status=${nextStatus}`);
    fetchHospitals();
  };

  const filtered = hospitals.filter(
    (h) =>
      h.name.toLowerCase().includes(search.toLowerCase()) ||
      h.city.toLowerCase().includes(search.toLowerCase()) ||
      h.registrationNo.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Hospitals Directory</h1>
          <p className="page-subtitle">Provision transplant centers, verify organ licenses, and manage operational status</p>
        </div>
        <Button variant="primary" icon={Plus} onClick={() => setIsModalOpen(true)}>
          Add Hospital
        </Button>
      </div>

      <div style={{ marginBottom: '16px', maxWidth: '360px' }}>
        <Input
          placeholder="Search by name, city, or registration no..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      <div className="ui-table-wrapper">
        <table className="ui-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Hospital Name</th>
              <th>Reg. No.</th>
              <th>Type</th>
              <th>City</th>
              <th>Licensed Organs</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((h) => (
              <tr key={h.id}>
                <td>#{h.id}</td>
                <td style={{ fontWeight: 600 }}>{h.name}</td>
                <td>{h.registrationNo}</td>
                <td>{h.hospitalType}</td>
                <td>{h.city}</td>
                <td className="text-caption">{h.licensedOrgans}</td>
                <td><Badge status={h.status} /></td>
                <td style={{ display: 'flex', gap: '8px' }}>
                  <Button
                    variant="ghost"
                    size="small"
                    icon={KeyRound}
                    onClick={() => navigate(`/admin/security-codes?hospitalId=${h.id}`)}
                  >
                    Code
                  </Button>
                  {h.status !== 'SUSPENDED' ? (
                    <Button variant="danger" size="small" onClick={() => handleStatusChange(h.id, 'SUSPENDED')}>
                      Suspend
                    </Button>
                  ) : (
                    <Button variant="success" size="small" onClick={() => handleStatusChange(h.id, 'ACTIVE')}>
                      Activate
                    </Button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Register New Transplant Hospital">
        {error && <p style={{ color: 'var(--color-danger)', marginBottom: '12px', fontSize: '14px' }}>{error}</p>}
        <form onSubmit={handleCreate} style={{ maxHeight: '70vh', overflowY: 'auto', paddingRight: '8px' }}>
          <Input label="Hospital Name" required value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
          <div className="grid-2">
            <Input label="Registration No." required value={form.registrationNo} onChange={(e) => setForm({ ...form, registrationNo: e.target.value })} />
            <Select
              label="Hospital Type"
              value={form.hospitalType}
              onChange={(e) => setForm({ ...form, hospitalType: e.target.value })}
              options={[
                { value: 'GOVERNMENT', label: 'Government' },
                { value: 'PRIVATE', label: 'Private' },
                { value: 'TRUST', label: 'Trust' },
              ]}
            />
          </div>
          <Input label="Licensed Organs (Comma-separated)" required value={form.licensedOrgans} onChange={(e) => setForm({ ...form, licensedOrgans: e.target.value })} />
          <div className="grid-2">
            <Input label="Contact Person" required value={form.contactPerson} onChange={(e) => setForm({ ...form, contactPerson: e.target.value })} />
            <Input label="Official Email" type="email" required value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
          </div>
          <div className="grid-2">
            <Input label="Phone" required value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
            <Input label="24x7 Emergency Phone" required value={form.emergencyPhone} onChange={(e) => setForm({ ...form, emergencyPhone: e.target.value })} />
          </div>
          <Input label="Street Address" required value={form.address} onChange={(e) => setForm({ ...form, address: e.target.value })} />
          <div className="grid-3">
            <Input label="City" required value={form.city} onChange={(e) => setForm({ ...form, city: e.target.value })} />
            <Input label="State" required value={form.state} onChange={(e) => setForm({ ...form, state: e.target.value })} />
            <Input label="Pincode" required value={form.pincode} onChange={(e) => setForm({ ...form, pincode: e.target.value })} />
          </div>
          <div className="grid-2">
            <Input label="Latitude" type="number" step="any" required value={form.latitude} onChange={(e) => setForm({ ...form, latitude: e.target.value })} />
            <Input label="Longitude" type="number" step="any" required value={form.longitude} onChange={(e) => setForm({ ...form, longitude: e.target.value })} />
          </div>
          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '16px' }}>
            <Button variant="secondary" onClick={() => setIsModalOpen(false)}>Cancel</Button>
            <Button type="submit" variant="primary" loading={saving}>Save Hospital</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
}


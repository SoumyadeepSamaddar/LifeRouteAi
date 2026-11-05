import React, { useEffect, useState } from 'react';
import { KeyRound, Copy, Check } from 'lucide-react';
import axiosClient from '../../api/axiosClient';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { Select } from '../../components/ui/Input';
import { Modal } from '../../components/ui/Modal';

export default function SecurityCodesPage() {
  const [codes, setCodes] = useState([]);
  const [hospitals, setHospitals] = useState([]);
  const [selectedHospitalId, setSelectedHospitalId] = useState('');
  const [validHours, setValidHours] = useState(72);
  const [generatedCode, setGeneratedCode] = useState(null);
  const [copied, setCopied] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const loadData = async () => {
    const [cRes, hRes] = await Promise.all([
      axiosClient.get('/admin/security-codes'),
      axiosClient.get('/admin/hospitals'),
    ]);
    setCodes(cRes.data || []);
    setHospitals(hRes.data || []);
    if (hRes.data?.length > 0 && !selectedHospitalId) {
      setSelectedHospitalId(hRes.data[0].id);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleGenerate = async (e) => {
    e.preventDefault();
    const res = await axiosClient.post(`/admin/security-codes?hospitalId=${selectedHospitalId}&validHours=${validHours}`);
    setGeneratedCode(res.data);
    loadData();
  };

  const handleRevoke = async (id) => {
    await axiosClient.post(`/admin/security-codes/${id}/revoke`);
    loadData();
  };

  const copyToClipboard = () => {
    if (generatedCode?.rawCode) {
      navigator.clipboard.writeText(generatedCode.rawCode);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Hospital Security Codes</h1>
          <p className="page-subtitle">Generate single-use BCrypt-hashed activation codes (Section 6.3 & Page A5)</p>
        </div>
        <Button variant="primary" icon={KeyRound} onClick={() => { setGeneratedCode(null); setIsModalOpen(true); }}>
          Generate Code
        </Button>
      </div>

      <div className="ui-table-wrapper">
        <table className="ui-table">
          <thead>
            <tr>
              <th>Masked Code</th>
              <th>Hospital</th>
              <th>Status</th>
              <th>Expires At</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {codes.map((c) => (
              <tr key={c.id}>
                <td style={{ fontFamily: 'monospace', fontWeight: 700 }}>{c.codeMasked}</td>
                <td>{c.hospitalName} (#{c.hospitalId})</td>
                <td><Badge status={c.status} /></td>
                <td className="text-caption">{c.expiresAt}</td>
                <td>
                  {c.status === 'ACTIVE' && (
                    <Button variant="danger" size="small" onClick={() => handleRevoke(c.id)}>
                      Revoke
                    </Button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Generate Hospital Activation Code">
        {!generatedCode ? (
          <form onSubmit={handleGenerate}>
            <Select
              label="Select Hospital"
              required
              value={selectedHospitalId}
              onChange={(e) => setSelectedHospitalId(e.target.value)}
              options={hospitals.map((h) => ({ value: h.id, label: `${h.name} (${h.city})` }))}
            />
            <Select
              label="Validity Period"
              value={validHours}
              onChange={(e) => setValidHours(e.target.value)}
              options={[
                { value: 24, label: '24 Hours' },
                { value: 72, label: '72 Hours (Recommended)' },
                { value: 168, label: '7 Days' },
              ]}
            />
            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '20px' }}>
              <Button variant="secondary" onClick={() => setIsModalOpen(false)}>Cancel</Button>
              <Button type="submit" variant="primary">Generate Code</Button>
            </div>
          </form>
        ) : (
          <div style={{ textAlign: 'center' }}>
            <div style={{ background: '#FEF3C7', color: '#92400E', padding: '10px', borderRadius: '8px', fontSize: '13px', marginBottom: '16px' }}>
              Copy this code now. Only its cryptographic hash is stored in MySQL—it will never be shown again.
            </div>
            <div style={{ fontSize: '26px', fontWeight: 700, fontFamily: 'monospace', padding: '16px', background: 'var(--color-bg)', borderRadius: '8px', border: '1px dashed var(--color-primary)', marginBottom: '16px' }}>
              {generatedCode.rawCode}
            </div>
            <div style={{ display: 'flex', justifyContent: 'center', gap: '12px' }}>
              <Button variant="secondary" icon={copied ? Check : Copy} onClick={copyToClipboard}>
                {copied ? 'Copied!' : 'Copy Code'}
              </Button>
              <Button variant="primary" onClick={() => setIsModalOpen(false)}>Done</Button>
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
}


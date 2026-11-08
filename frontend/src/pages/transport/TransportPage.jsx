import React, { useEffect, useState } from 'react';
import { Route, Plane, TrainFront, AlertTriangle, RefreshCcw, BellRing, ShieldCheck } from 'lucide-react';
import axiosClient from '../../api/axiosClient';
import { Button } from '../../components/ui/Button';
import { Card } from '../../components/ui/Card';

const defaultRequest = {
  donorLatitude: 19.0760,
  donorLongitude: 72.8777,
  recipientLatitude: 18.5204,
  recipientLongitude: 73.8567,
  organType: 'LIVER',
  trafficLevel: 'MODERATE',
  hasAirport: true,
  hasHelipad: false,
  trainAvailable: true,
  elapsedSinceRetrievalMinutes: 120,
};

export default function TransportPage() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [data, setData] = useState(null);
  const [hazards, setHazards] = useState([]);
  const [alerts, setAlerts] = useState([]);

  const fetchOptions = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await axiosClient.post('/transport/options', defaultRequest);
      setData(res.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Unable to calculate route options right now.');
    } finally {
      setLoading(false);
    }
  };

  const fetchHazards = async () => {
    try {
      const res = await axiosClient.get('/transport/hazards');
      setHazards(res.data || []);
    } catch {
      setHazards([]);
    }
  };

  const fetchAlerts = async () => {
    try {
      const res = await axiosClient.get('/transport/alerts');
      setAlerts(res.data || []);
    } catch {
      setAlerts([]);
    }
  };

  useEffect(() => {
    fetchOptions();
    fetchHazards();
    fetchAlerts();
  }, []);

  if (loading) {
    return (
      <div style={{ display: 'grid', gap: '16px' }}>
        <div style={{ height: '90px', background: '#E2E8F0', borderRadius: '12px', animation: 'pulse 1.5s ease-in-out infinite' }} />
        <div style={{ height: '220px', background: '#E2E8F0', borderRadius: '12px', animation: 'pulse 1.5s ease-in-out infinite' }} />
      </div>
    );
  }

  if (error) {
    return (
      <Card title="Transport status" subtitle="Route evaluation failed">
        <p style={{ color: 'var(--color-danger)', marginBottom: '16px' }}>{error}</p>
        <Button variant="primary" icon={RefreshCcw} onClick={fetchOptions}>Retry route check</Button>
      </Card>
    );
  }

  if (!data || !data.options || data.options.length === 0) {
    return (
      <Card title="No route available" subtitle="No transport options were found for this case.">
        <Button variant="primary" icon={Route} onClick={fetchOptions}>Check route again</Button>
      </Card>
    );
  }

  return (
    <div className="transport-shell">
      <div className="transport-header page-header">
        <div>
          <div className="eyebrow">Logistics overview</div>
          <h1>Transport & route options</h1>
          <p className="page-subtitle">Road, air and rail comparison for the matched donor-recipient case</p>
        </div>
        <Button variant="primary" icon={RefreshCcw} onClick={fetchOptions}>Refresh route</Button>
      </div>

      <div className="transport-hero">
        <div className="transport-hero-card transport-hero-primary">
          <div className="transport-hero-label">Selected mode</div>
          <div className="transport-hero-value">{data.selectedMode || 'Not selected'}</div>
          <div className="transport-hero-meta">Backup: {data.backupMode || 'None'}</div>
        </div>

        <div className="transport-hero-card transport-hero-secondary">
          <div className="transport-hero-label">Estimated ETA</div>
          <div className="transport-hero-value">{Math.round(data.etaMinutes || 0)} min</div>
          <div className="transport-hero-meta">Total travel time</div>
        </div>

        <div className="transport-hero-card transport-hero-plain">
          <div className="transport-hero-label">Route summary</div>
          <div className="transport-hero-summary">{data.summary}</div>
        </div>
      </div>

      <div className="route-map-panel">
        <div className="route-map-header">
          <div>
            <div className="route-map-tag">Live route view</div>
            <h3>Donor to recipient corridor</h3>
          </div>
          <span className="route-map-status">ACTIVE</span>
        </div>
        <div className="route-map-canvas">
          <div className="route-map-marker donor">Donor</div>
          <div className="route-map-marker recipient">Recipient</div>
          <div className="route-map-line route-map-line-main" />
          <div className="route-map-line route-map-line-alt" />
          <div className="route-map-badge badge-traffic">Traffic: {defaultRequest.trafficLevel}</div>
          <div className="route-map-badge badge-distance">{data.options[0]?.distanceKm || 0} km</div>
        </div>
      </div>

      <div className="grid-stats">
        <Card title="Selected mode" subtitle={data.selectedMode || 'Not selected'}>
          <div className="metric-value">{data.selectedMode}</div>
        </Card>
        <Card title="Backup mode" subtitle={data.backupMode || 'None'}>
          <div className="metric-value">{data.backupMode || 'None'}</div>
        </Card>
        <Card title="Estimated ETA" subtitle="Total travel time">
          <div className="metric-value">{Math.round(data.etaMinutes || 0)} min</div>
        </Card>
      </div>

      <Card title="Available options" subtitle={data.summary}>
        <div className="transport-option-list">
          {data.options.map((option) => {
            const modeIcon = option.mode === 'AIR' ? Plane : option.mode === 'TRAIN' ? TrainFront : Route;
            const Icon = modeIcon;

            return (
              <div
                key={option.mode}
                className={`transport-option-item ${option.isSelected ? 'selected' : ''}`}
              >
                <div className="transport-option-icon-wrap">
                  <Icon size={18} color="var(--color-primary)" />
                </div>
                <div className="transport-option-body">
                  <div className="transport-option-mode">{option.mode}</div>
                  <div className="transport-option-meta">
                    {option.distanceKm} km • {Math.round(option.etaMinutes)} min ETA • {option.reason}
                  </div>
                </div>
                <div className="transport-option-status">
                  <div className={`status-pill ${option.viable ? 'success' : 'danger'}`}>{option.viable ? 'Viable' : 'Blocked'}</div>
                  {option.viable && <div className="transport-option-buffer">{Math.round(option.remainingBufferMinutes)} min buffer</div>}
                </div>
              </div>
            );
          })}
        </div>
      </Card>

      <div className="grid-2">
        <Card title="Hazard awareness" subtitle="Operational watchlist">
          <div className="alert-stack">
            {hazards.length === 0 ? (
              <div className="empty-state">No active hazard flags in the system.</div>
            ) : (
              hazards.map((hazard) => (
                <div key={hazard.id} className="alert-card warning">
                  <div className="alert-card-header">
                    <div className="alert-title-row">
                      <AlertTriangle size={16} />
                      <strong>{hazard.title || hazard.hazardType}</strong>
                    </div>
                    <span className="status-text">{hazard.status}</span>
                  </div>
                  <div className="alert-card-meta">
                    {hazard.area} • {hazard.severity} • {hazard.affectedRoads || 'General corridor'}
                  </div>
                </div>
              ))
            )}
          </div>
        </Card>

        <Card title="Alert center" subtitle="Recent notifications">
          <div className="alert-stack">
            {alerts.length === 0 ? (
              <div className="empty-state">No alerts have been generated for this case.</div>
            ) : (
              alerts.map((alert) => (
                <div key={alert.id} className="alert-card info">
                  <div className="alert-title-row">
                    <BellRing size={16} color="var(--color-primary)" />
                    <strong>{alert.alertType}</strong>
                  </div>
                  <div className="alert-card-meta">{alert.channel} • {alert.deliveryStatus}</div>
                </div>
              ))
            )}
          </div>
        </Card>
      </div>

      <Card title="Green corridor actions" subtitle="Request traffic coordination and validation">
        <div className="transport-actions-row">
          <Button variant="primary" icon={ShieldCheck} onClick={async () => {
            try {
              await axiosClient.post('/transport/green-corridor', {
                routeId: 1,
                policeContact: 'Traffic Control Desk',
                referenceNote: 'Emergency donor transfer - road priority approval required.',
                requestedBy: 1,
              });
              fetchAlerts();
            } catch (err) {
              console.error('Green corridor request failed', err);
            }
          }}>Request green corridor</Button>
        </div>
      </Card>
    </div>
  );
}

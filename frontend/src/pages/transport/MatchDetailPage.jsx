import React, { useEffect, useMemo, useState } from 'react';
import { useParams } from 'react-router-dom';
import { AlertTriangle, ArrowRightLeft, Clock3, HeartPulse, ShieldCheck, Route as RouteIcon, BellRing } from 'lucide-react';
import axiosClient from '../../api/axiosClient';
import { Button } from '../../components/ui/Button';
import { Card } from '../../components/ui/Card';
import VehicleTrackingCard from './VehicleTrackingCard';

const sampleRouteRequest = {
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

export default function MatchDetailPage() {
  const { matchId } = useParams();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [routeData, setRouteData] = useState(null);
  const [hazards, setHazards] = useState([]);
  const [alerts, setAlerts] = useState([]);

  const loadData = async () => {
    setLoading(true);
    setError('');
    try {
      const [routeRes, hazardsRes, alertsRes] = await Promise.all([
        axiosClient.post('/transport/options', sampleRouteRequest),
        axiosClient.get('/transport/hazards'),
        axiosClient.get('/transport/alerts'),
      ]);

      setRouteData(routeRes.data);
      setHazards(hazardsRes.data || []);
      setAlerts(alertsRes.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Unable to load the transport detail page.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, [matchId]);

  const selectedOption = useMemo(() => {
    if (!routeData || !routeData.options) return null;
    return routeData.options.find((option) => option.isSelected) || routeData.options[0];
  }, [routeData]);

  if (loading) {
    return (
      <div style={{ display: 'grid', gap: '16px' }}>
        <div style={{ height: '90px', background: '#E2E8F0', borderRadius: '12px', animation: 'pulse 1.5s ease-in-out infinite' }} />
        <div style={{ height: '200px', background: '#E2E8F0', borderRadius: '12px', animation: 'pulse 1.5s ease-in-out infinite' }} />
      </div>
    );
  }

  if (error) {
    return (
      <Card title="Match detail unavailable" subtitle="Unable to load the route and alert summary">
        <p style={{ color: 'var(--color-danger)', marginBottom: '16px' }}>{error}</p>
        <Button variant="primary" onClick={loadData}>Retry</Button>
      </Card>
    );
  }

  return (
    <div className="match-shell">
      <div className="page-header match-header">
        <div>
          <div className="eyebrow">Case overview</div>
          <h1>Match detail</h1>
          <p className="page-subtitle">Case ID: {matchId || 'LR-MATCH-2048'} • Donor → Recipient transport review</p>
        </div>
        <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
          <Button variant="secondary">View clinical summary</Button>
          <Button variant="primary">Confirm route</Button>
        </div>
      </div>

      <div className="grid-stats">
        <Card title="Transport status" subtitle="Current route choice">
          <div className="metric-value">{selectedOption?.mode || 'ROAD'}</div>
        </Card>
        <Card title="ETA" subtitle="Travel time to theater">
          <div className="metric-value">{Math.round(routeData?.etaMinutes || 0)} min</div>
        </Card>
        <Card title="Ischemia left" subtitle="Time remaining">
          <div className="metric-value">{Math.max(0, Math.round((selectedOption?.remainingBufferMinutes || 120)))} min</div>
        </Card>
      </div>

      <div className="match-hero-grid">
        <Card title="Route overview" subtitle="Selected transport path">
          <div className="detail-list">
            <div className="detail-row detail-header">
              <span className="detail-row-label"><RouteIcon size={15} /> Selected route</span>
              <strong>{routeData?.selectedMode || 'ROAD'}</strong>
            </div>
            <div className="detail-row">
              <span>Distance</span>
              <strong>{selectedOption?.distanceKm || 0} km</strong>
            </div>
            <div className="detail-row">
              <span>Travel time</span>
              <strong>{Math.round(selectedOption?.etaMinutes || 0)} min</strong>
            </div>
            <div className="detail-row">
              <span>Safety buffer</span>
              <strong>{Math.round(selectedOption?.remainingBufferMinutes || 0)} min</strong>
            </div>
            <div className="detail-row">
              <span>Traffic status</span>
              <strong>{sampleRouteRequest.trafficLevel}</strong>
            </div>
          </div>
        </Card>

        <div className="route-map-panel compact-map">
          <div className="route-map-header">
            <div>
              <div className="route-map-tag">Live corridor map</div>
              <h3>Route line</h3>
            </div>
            <span className="route-map-status">TRACKING</span>
          </div>
          <div className="route-map-canvas smaller-map">
            <div className="route-map-marker donor">Donor</div>
            <div className="route-map-marker recipient">Recipient</div>
            <div className="route-map-line route-map-line-main" />
            <div className="route-map-line route-map-line-alt" />
            <div className="route-map-badge badge-traffic">{sampleRouteRequest.trafficLevel}</div>
          </div>
        </div>

        <Card title="Green corridor" subtitle="Traffic control and validation">
          <div className="detail-list">
            <div className="detail-row status-ok">
              <span className="detail-row-label"><ShieldCheck size={15} /> Status</span>
              <strong>REQUESTED</strong>
            </div>
            <div className="detail-row">
              <span className="detail-row-label"><BellRing size={15} /> Police desk</span>
              <strong>Traffic Control Desk</strong>
            </div>
            <div className="detail-row">
              <span className="detail-row-label"><Clock3 size={15} /> Dispatch window</span>
              <strong>12 min</strong>
            </div>
          </div>
        </Card>
      </div>

      <div className="grid-2">
        <Card title="Route options" subtitle="Mode comparison">
          <div className="transport-option-list compact">
            {(routeData?.options || []).map((option) => (
              <div key={option.mode} className={`transport-option-item ${option.isSelected ? 'selected' : ''}`}>
                <div className="transport-option-body">
                  <div className="transport-option-mode">{option.mode}</div>
                  <div className="transport-option-meta">
                    {option.distanceKm} km • {Math.round(option.etaMinutes)} min • {Math.round(option.remainingBufferMinutes)} min buffer
                  </div>
                </div>
                <div className="transport-option-status">
                  <span className={`status-pill ${option.viable ? 'success' : 'danger'}`}>{option.viable ? 'Viable' : 'Blocked'}</span>
                </div>
              </div>
            ))}
          </div>
        </Card>

        <VehicleTrackingCard />
      </div>

      <div className="grid-2">
        <Card title="Operational watch" subtitle="Hazards and active alerts">
          <div className="alert-stack">
            {hazards.length === 0 ? (
              <div className="empty-state">No active hazard flags.</div>
            ) : (
              hazards.map((hazard) => (
                <div key={hazard.id} className="alert-card warning">
                  <div className="alert-card-header">
                    <div className="alert-title-row">
                      <AlertTriangle size={15} />
                      <strong>{hazard.title || hazard.hazardType}</strong>
                    </div>
                    <span className="status-text">{hazard.severity}</span>
                  </div>
                  <div className="alert-card-meta">{hazard.area} • {hazard.status}</div>
                </div>
              ))
            )}

            {alerts.length > 0 && (
              <div className="alert-stack nested">
                {alerts.slice(0, 3).map((alert) => (
                  <div key={alert.id} className="alert-card info">
                    <div className="alert-title-row">
                      <BellRing size={15} color="var(--color-primary)" />
                      <strong>{alert.alertType}</strong>
                    </div>
                    <div className="alert-card-meta">{alert.channel} • {alert.deliveryStatus}</div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </Card>
      </div>
    </div>
  );
}

import React, { useEffect, useState } from 'react';
import { CarFront, MapPinned } from 'lucide-react';
import axiosClient from '../../api/axiosClient';

export default function VehicleTrackingCard() {
  const [locations, setLocations] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await axiosClient.get('/transport/vehicle-locations');
        setLocations(res.data || []);
      } catch {
        setLocations([]);
      }
    };

    fetchData();
    const id = setInterval(fetchData, 15000);
    return () => clearInterval(id);
  }, []);

  const latest = locations[locations.length - 1];

  return (
    <div style={{ border: '1px solid var(--color-border)', borderRadius: '12px', padding: '16px', background: 'var(--color-surface)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
        <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
          <CarFront size={18} color="var(--color-primary)" />
          <strong>Vehicle tracking</strong>
        </div>
        <div style={{ fontSize: '12px', color: 'var(--color-text-muted)' }}>{latest ? 'Live' : 'Offline'}</div>
      </div>

      {latest ? (
        <div style={{ display: 'grid', gap: '8px', fontSize: '14px', color: 'var(--color-text-muted)' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <MapPinned size={14} color="var(--color-primary)" />
            {latest.latitude}, {latest.longitude}
          </div>
          <div>Speed: {latest.speedKmh ?? 0} km/h</div>
          <div>Last update: {latest.timestamp ? new Date(latest.timestamp).toLocaleString('en-IN', { timeZone: 'Asia/Kolkata' }) : 'Now'}</div>
        </div>
      ) : (
        <div style={{ color: 'var(--color-text-muted)' }}>No live vehicle position is available yet.</div>
      )}
    </div>
  );
}

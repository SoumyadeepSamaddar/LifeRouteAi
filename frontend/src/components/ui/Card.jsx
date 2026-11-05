import React from 'react';

export const Card = ({ title, subtitle, action, children, style }) => (
  <div
    style={{
      background: 'var(--color-surface)',
      borderRadius: 'var(--radius-card)',
      border: '1px solid var(--color-border)',
      padding: 'var(--space-6)',
      boxShadow: 'var(--shadow-card)',
      ...style,
    }}
  >
    {(title || action) && (
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
        <div>
          {title && <h3>{title}</h3>}
          {subtitle && <p className="text-caption" style={{ marginTop: '2px' }}>{subtitle}</p>}
        </div>
        {action && <div>{action}</div>}
      </div>
    )}
    {children}
  </div>
);

export const StatCard = ({ label, value, trend, icon: Icon, accentColor = 'var(--color-primary)' }) => (
  <div
    style={{
      background: 'var(--color-surface)',
      borderRadius: 'var(--radius-card)',
      border: '1px solid var(--color-border)',
      padding: '20px',
      boxShadow: 'var(--shadow-card)',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'flex-start',
    }}
  >
    <div>
      <p className="text-small" style={{ color: 'var(--color-text-muted)', fontWeight: 500 }}>{label}</p>
      <div style={{ fontSize: '28px', fontWeight: 700, color: 'var(--color-secondary)', marginTop: '8px' }}>
        {value}
      </div>
      {trend && <p className="text-caption" style={{ marginTop: '6px', color: accentColor }}>{trend}</p>}
    </div>
    {Icon && (
      <div style={{ padding: '10px', borderRadius: '10px', background: 'var(--color-bg)', color: accentColor }}>
        <Icon size={22} />
      </div>
    )}
  </div>
);

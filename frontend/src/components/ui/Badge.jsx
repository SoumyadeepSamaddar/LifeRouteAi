import React from 'react';

const STATUS_MAP = {
  ACTIVE: { bg: '#DCFCE7', text: '#166534' },
  AVAILABLE: { bg: '#DCFCE7', text: '#166534' },
  CONFIRMED: { bg: '#DCFCE7', text: '#166534' },
  VALIDATED: { bg: '#DCFCE7', text: '#166534' },
  HOLD: { bg: '#FEF3C7', text: '#92400E' },
  PENDING_ACTIVATION: { bg: '#FEF3C7', text: '#92400E' },
  PENDING_CONFIRMATION: { bg: '#FEF3C7', text: '#92400E' },
  REQUESTED: { bg: '#FEF3C7', text: '#92400E' },
  MATCHED: { bg: '#CCFBF1', text: '#115E59' },
  ALLOCATED: { bg: '#CCFBF1', text: '#115E59' },
  SEARCHING: { bg: '#CCFBF1', text: '#115E59' },
  USED: { bg: '#DBEAFE', text: '#1E40AF' },
  IN_TRANSIT: { bg: '#DBEAFE', text: '#1E40AF' },
  TRANSPLANTED: { bg: '#166534', text: '#FFFFFF' },
  COMPLETED: { bg: '#166534', text: '#FFFFFF' },
  REJECTED: { bg: '#FEE2E2', text: '#991B1B' },
  EXPIRED: { bg: '#FEE2E2', text: '#991B1B' },
  REVOKED: { bg: '#FEE2E2', text: '#991B1B' },
  SUSPENDED: { bg: '#FEE2E2', text: '#991B1B' },
};

export const Badge = ({ status, children }) => {
  const key = (status || children || '').toString().toUpperCase();
  const palette = STATUS_MAP[key] || { bg: '#E2E8F0', text: '#475569' };

  return (
    <span
      style={{
        backgroundColor: palette.bg,
        color: palette.text,
        padding: '4px 10px',
        borderRadius: 'var(--radius-pill)',
        fontSize: '12px',
        fontWeight: 600,
        display: 'inline-flex',
        alignItems: 'center',
        gap: '4px',
      }}
    >
      {children || status}
    </span>
  );
};

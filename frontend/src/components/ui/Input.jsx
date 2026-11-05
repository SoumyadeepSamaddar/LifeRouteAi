import React from 'react';

export const Input = ({ label, required, helperText, error, unit, style, ...props }) => (
  <div style={{ marginBottom: '16px', ...style }}>
    {label && (
      <label style={{ display: 'block', fontSize: '14px', fontWeight: 600, marginBottom: '6px', color: 'var(--color-text)' }}>
        {label} {required && <span style={{ color: 'var(--color-danger)' }}>*</span>}
      </label>
    )}
    <div style={{ display: 'flex', alignItems: 'center' }}>
      <input
        required={required}
        style={{
          width: '100%',
          height: '44px',
          padding: '0 12px',
          borderRadius: 'var(--radius-control)',
          border: `1px solid ${error ? 'var(--color-danger)' : 'var(--color-border)'}`,
          fontSize: '15px',
          fontFamily: 'inherit',
          background: '#FFFFFF',
          color: 'var(--color-text)',
        }}
        {...props}
      />
      {unit && (
        <span style={{ marginLeft: '-40px', fontSize: '13px', color: 'var(--color-text-muted)', pointerEvents: 'none' }}>
          {unit}
        </span>
      )}
    </div>
    {error && <p style={{ color: 'var(--color-danger)', fontSize: '12px', marginTop: '4px' }}>{error}</p>}
    {!error && helperText && <p className="text-caption" style={{ marginTop: '4px' }}>{helperText}</p>}
  </div>
);

export const Select = ({ label, required, options = [], children, style, ...props }) => (
  <div style={{ marginBottom: '16px', ...style }}>
    {label && (
      <label style={{ display: 'block', fontSize: '14px', fontWeight: 600, marginBottom: '6px', color: 'var(--color-text)' }}>
        {label} {required && <span style={{ color: 'var(--color-danger)' }}>*</span>}
      </label>
    )}
    <select
      required={required}
      style={{
        width: '100%',
        height: '44px',
        padding: '0 12px',
        borderRadius: 'var(--radius-control)',
        border: '1px solid var(--color-border)',
        fontSize: '15px',
        fontFamily: 'inherit',
        background: '#FFFFFF',
        color: 'var(--color-text)',
      }}
      {...props}
    >
      {options.map((opt) => (
        <option key={opt.value} value={opt.value}>{opt.label}</option>
      ))}
      {children}
    </select>
  </div>
);

import React from 'react';

const variantStyles = {
  primary: { background: 'var(--color-primary)', color: '#FFFFFF', border: 'none' },
  secondary: { background: '#FFFFFF', color: 'var(--color-primary)', border: '1.5px solid var(--color-primary)' },
  success: { background: 'var(--color-success)', color: '#FFFFFF', border: 'none' },
  danger: { background: 'var(--color-danger)', color: '#FFFFFF', border: 'none' },
  emergency: { background: 'var(--color-accent)', color: '#FFFFFF', border: 'none' },
  ghost: { background: 'transparent', color: 'var(--color-primary)', border: 'none' },
};

const sizeStyles = {
  small: { height: '32px', padding: '0 12px', fontSize: '14px' },
  medium: { height: '40px', padding: '0 16px', fontSize: '15px' },
  large: { height: '48px', padding: '0 24px', fontSize: '16px' },
};

export const Button = ({
  variant = 'primary',
  size = 'medium',
  type = 'button',
  disabled = false,
  loading = false,
  icon: Icon,
  children,
  style,
  ...props
}) => {
  const isDisabled = disabled || loading;
  const baseStyle = {
    borderRadius: 'var(--radius-control)',
    fontWeight: 600,
    fontFamily: 'inherit',
    cursor: isDisabled ? 'not-allowed' : 'pointer',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    gap: '8px',
    transition: 'all 0.15s ease',
    ...(isDisabled
      ? { background: 'var(--color-disabled)', color: 'var(--color-text-muted)', border: 'none' }
      : variantStyles[variant] || variantStyles.primary),
    ...(sizeStyles[size] || sizeStyles.medium),
    ...style,
  };

  return (
    <button type={type} disabled={isDisabled} style={baseStyle} {...props}>
      {Icon && <Icon size={size === 'small' ? 16 : 18} />}
      {loading ? 'Processing...' : children}
    </button>
  );
};

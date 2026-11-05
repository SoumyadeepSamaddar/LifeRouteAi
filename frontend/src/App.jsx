import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ProtectedRoute } from './components/layout/ProtectedRoute';
import { AppLayout } from './components/layout/AppLayout';

import LandingPage from './pages/public/LandingPage';
import LoginPage from './pages/public/LoginPage';
import HospitalActivationPage from './pages/public/HospitalActivationPage';
import ErrorPage from './pages/public/ErrorPage';

import AdminDashboard from './pages/admin/AdminDashboard';
import HospitalsPage from './pages/admin/HospitalsPage';
import SecurityCodesPage from './pages/admin/SecurityCodesPage';
import SystemSettingsPage from './pages/admin/SystemSettingsPage';
import AuditLogPage from './pages/admin/AuditLogPage';
import CivicPointsPage from './pages/admin/CivicPointsPage';
import HospitalStaffPage from './pages/hospital/HospitalStaffPage';
import UserPortalPage from './pages/portal/UserPortalPage';

import './styles/tokens.css';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public Pages (P1 - P6) */}
          <Route path="/" element={<LandingPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/hospital/activate" element={<HospitalActivationPage />} />
          <Route path="/403" element={<ErrorPage code={403} title="Access Denied" message="You do not have permission to view this resource." />} />

          {/* Admin Routes (A1 - A12) */}
          <Route path="/admin" element={<ProtectedRoute allowedRoles={['ADMIN']}><AppLayout><AdminDashboard /></AppLayout></ProtectedRoute>} />
          <Route path="/admin/hospitals" element={<ProtectedRoute allowedRoles={['ADMIN']}><AppLayout><HospitalsPage /></AppLayout></ProtectedRoute>} />
          <Route path="/admin/security-codes" element={<ProtectedRoute allowedRoles={['ADMIN']}><AppLayout><SecurityCodesPage /></AppLayout></ProtectedRoute>} />
          <Route path="/admin/staff" element={<ProtectedRoute allowedRoles={['ADMIN']}><AppLayout><HospitalStaffPage /></AppLayout></ProtectedRoute>} />
          <Route path="/admin/civic-points" element={<ProtectedRoute allowedRoles={['ADMIN']}><AppLayout><CivicPointsPage /></AppLayout></ProtectedRoute>} />
          <Route path="/admin/settings" element={<ProtectedRoute allowedRoles={['ADMIN']}><AppLayout><SystemSettingsPage /></AppLayout></ProtectedRoute>} />
          <Route path="/admin/audit" element={<ProtectedRoute allowedRoles={['ADMIN']}><AppLayout><AuditLogPage /></AppLayout></ProtectedRoute>} />

          {/* Hospital Routes */}
          <Route path="/hospital" element={<ProtectedRoute allowedRoles={['HOSPITAL', 'ADMIN']}><AppLayout><HospitalStaffPage /></AppLayout></ProtectedRoute>} />
          <Route path="/hospital/staff" element={<ProtectedRoute allowedRoles={['HOSPITAL', 'ADMIN']}><AppLayout><HospitalStaffPage /></AppLayout></ProtectedRoute>} />

          {/* Patient / Donor Portal */}
          <Route path="/me" element={<ProtectedRoute allowedRoles={['PATIENT', 'DONOR', 'ADMIN']}><AppLayout><UserPortalPage /></AppLayout></ProtectedRoute>} />

          {/* Catch-all 404 */}
          <Route path="*" element={<ErrorPage />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}


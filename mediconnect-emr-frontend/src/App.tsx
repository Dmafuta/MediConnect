import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import AppShell from './components/AppShell';
import LoginPage from './pages/LoginPage';
import PatientList from './pages/PatientList';
import PatientForm from './pages/PatientForm';
import AppointmentsPage from './pages/AppointmentsPage';
import VisitQueuePage from './pages/VisitQueuePage';
import LabOrdersPage from './pages/LabOrdersPage';
import PharmacyPage from './pages/PharmacyPage';
import ReportsPage from './pages/ReportsPage';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/*"
            element={
              <PrivateRoute>
                <AppShell>
                  <Routes>
                    <Route path="/patients" element={<PatientList />} />
                    <Route path="/patients/new" element={<PatientForm />} />
                    <Route path="/patients/:id/edit" element={<PatientForm />} />
                    <Route path="/appointments" element={<AppointmentsPage />} />
                    <Route path="/queue" element={<VisitQueuePage />} />
                    <Route path="/lab" element={<LabOrdersPage />} />
                    <Route path="/pharmacy" element={<PharmacyPage />} />
                    <Route path="/reports" element={<ReportsPage />} />
                    <Route path="/" element={<Navigate to="/patients" replace />} />
                  </Routes>
                </AppShell>
              </PrivateRoute>
            }
          />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;

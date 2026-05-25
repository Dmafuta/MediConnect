import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import PatientList from './pages/PatientList';
import PatientForm from './pages/PatientForm';
import './App.css'; // Keep existing CSS if needed

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<PatientList />} /> {/* Default route */}
          <Route path="/patients" element={<PatientList />} />
          <Route path="/patients/new" element={<PatientForm />} />
          <Route path="/patients/:id" element={<PatientForm />} /> {/* For viewing/editing */}
          <Route path="/patients/:id/edit" element={<PatientForm />} /> {/* Explicit edit route */}
          {/* Add other routes here as needed */}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
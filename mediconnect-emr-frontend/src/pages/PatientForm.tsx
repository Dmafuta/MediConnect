import React, { useEffect, useState } from 'react';
import {
  TextField,
  Button,
  Box,
  Typography,
  CircularProgress,
  MenuItem,
  Select,
  InputLabel,
  FormControl,
} from '@mui/material';
import type { SelectChangeEvent } from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { useNavigate, useParams } from 'react-router-dom';
import { createPatient, getPatientById, updatePatient, Patient } from '../api/patientApi';
import dayjs from 'dayjs';

const PatientForm: React.FC = () => {
  const { id } = useParams<{ id?: string }>();
  const navigate = useNavigate();
  const [patient, setPatient] = useState<Patient>({
    firstName: '',
    lastName: '',
    dateOfBirth: dayjs().format('YYYY-MM-DD'),
    gender: '',
    contactNumber: '',
    email: '',
    address: '',
    emergencyContactName: '',
    emergencyContactNumber: '',
    allergies: '',
    pastMedicalHistory: '',
    currentMedications: '',
  });
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (id) {
      setLoading(true);
      getPatientById(Number(id))
        .then((data) => {
          setPatient({
            ...data,
            dateOfBirth: dayjs(data.dateOfBirth).format('YYYY-MM-DD'),
          });
        })
        .catch((err) => {
          console.error('Error fetching patient:', err);
          setError('Failed to load patient data.');
        })
        .finally(() => setLoading(false));
    }
  }, [id]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setPatient((prev) => ({ ...prev, [name]: value }));
  };

  const handleSelectChange = (e: SelectChangeEvent) => {
    const { name, value } = e.target;
    setPatient((prev) => ({ ...prev, [name]: value }));
  };

  const handleDateChange = (date: dayjs.Dayjs | null) => {
    setPatient((prev) => ({ ...prev, dateOfBirth: date ? date.format('YYYY-MM-DD') : '' }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      if (id) {
        await updatePatient(Number(id), patient);
      } else {
        await createPatient(patient);
      }
      navigate('/patients');
    } catch (err) {
      console.error('Error saving patient:', err);
      setError('Failed to save patient data.');
    } finally {
      setLoading(false);
    }
  };

  if (loading && id && !patient.firstName) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Box component="form" onSubmit={handleSubmit} sx={{ padding: 3, maxWidth: 600, margin: 'auto' }}>
        <Typography variant="h4" component="h1" gutterBottom>
          {id ? 'Edit Patient' : 'Register New Patient'}
        </Typography>
        {error && <Typography color="error">{error}</Typography>}
        <TextField
          label="First Name"
          name="firstName"
          value={patient.firstName}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
        />
        <TextField
          label="Last Name"
          name="lastName"
          value={patient.lastName}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
        />
        <DatePicker
          label="Date of Birth"
          value={dayjs(patient.dateOfBirth)}
          onChange={handleDateChange}
          slots={{ textField: TextField }}
          slotProps={{ textField: { fullWidth: true, margin: 'normal', required: true } }}
        />
        <FormControl fullWidth margin="normal" required>
          <InputLabel>Gender</InputLabel>
          <Select
            name="gender"
            value={patient.gender}
            label="Gender"
            onChange={handleSelectChange}
          >
            <MenuItem value="Male">Male</MenuItem>
            <MenuItem value="Female">Female</MenuItem>
            <MenuItem value="Other">Other</MenuItem>
          </Select>
        </FormControl>
        <TextField
          label="Contact Number"
          name="contactNumber"
          value={patient.contactNumber}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
        />
        <TextField
          label="Email"
          name="email"
          type="email"
          value={patient.email}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
        />
        <TextField
          label="Address"
          name="address"
          value={patient.address}
          onChange={handleChange}
          fullWidth
          margin="normal"
        />
        <TextField
          label="Emergency Contact Name"
          name="emergencyContactName"
          value={patient.emergencyContactName}
          onChange={handleChange}
          fullWidth
          margin="normal"
        />
        <TextField
          label="Emergency Contact Number"
          name="emergencyContactNumber"
          value={patient.emergencyContactNumber}
          onChange={handleChange}
          fullWidth
          margin="normal"
        />
        <TextField
          label="Allergies"
          name="allergies"
          value={patient.allergies}
          onChange={handleChange}
          fullWidth
          margin="normal"
          multiline
          rows={2}
        />
        <TextField
          label="Past Medical History"
          name="pastMedicalHistory"
          value={patient.pastMedicalHistory}
          onChange={handleChange}
          fullWidth
          margin="normal"
          multiline
          rows={3}
        />
        <TextField
          label="Current Medications"
          name="currentMedications"
          value={patient.currentMedications}
          onChange={handleChange}
          fullWidth
          margin="normal"
          multiline
          rows={2}
        />
        <Button type="submit" variant="contained" color="primary" disabled={loading} sx={{ mt: 3, mr: 2 }}>
          {loading ? <CircularProgress size={24} /> : (id ? 'Update Patient' : 'Register Patient')}
        </Button>
        <Button variant="outlined" onClick={() => navigate('/patients')} sx={{ mt: 3 }}>
          Cancel
        </Button>
      </Box>
    </LocalizationProvider>
  );
};

export default PatientForm;

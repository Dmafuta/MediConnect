import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api'; // Assuming backend runs on 8080

export interface Patient {
  id?: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string; // Using string for simplicity, can be Date object
  gender: string;
  contactNumber: string;
  email: string;
  address?: string;
  emergencyContactName?: string;
  emergencyContactNumber?: string;
  allergies?: string;
  pastMedicalHistory?: string;
  currentMedications?: string;
}

export const getPatients = async (page: number = 0, size: number = 15) => {
  const response = await axios.get(`${API_BASE_URL}/patients`, {
    params: { page, size },
    headers: {
      // Add authorization header if needed
      // Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
    }
  });
  return response.data;
};

export const getPatientById = async (id: number) => {
  const response = await axios.get(`${API_BASE_URL}/patients/${id}`, {
    headers: {
      // Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
    }
  });
  return response.data;
};

export const createPatient = async (patient: Patient) => {
  const response = await axios.post(`${API_BASE_URL}/patients`, patient, {
    headers: {
      // Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
    }
  });
  return response.data;
};

export const updatePatient = async (id: number, patient: Patient) => {
  const response = await axios.put(`${API_BASE_URL}/patients/${id}`, patient, {
    headers: {
      // Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
    }
  });
  return response.data;
};

export const deletePatient = async (id: number) => {
  const response = await axios.delete(`${API_BASE_URL}/patients/${id}`, {
    headers: {
      // Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
    }
  });
  return response.data;
};

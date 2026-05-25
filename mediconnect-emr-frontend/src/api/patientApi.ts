import axiosInstance from './axiosInstance';

export interface Patient {
  id?: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
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
  const response = await axiosInstance.get('/patients', { params: { page, size } });
  return response.data;
};

export const getPatientById = async (id: number) => {
  const response = await axiosInstance.get(`/patients/${id}`);
  return response.data;
};

export const createPatient = async (patient: Patient) => {
  const response = await axiosInstance.post('/patients', patient);
  return response.data;
};

export const updatePatient = async (id: number, patient: Patient) => {
  const response = await axiosInstance.put(`/patients/${id}`, patient);
  return response.data;
};

export const deletePatient = async (id: number) => {
  const response = await axiosInstance.delete(`/patients/${id}`);
  return response.data;
};

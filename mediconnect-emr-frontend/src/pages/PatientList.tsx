import React, { useEffect, useState } from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  CircularProgress,
  Box,
  Typography,
  TablePagination,
} from '@mui/material';
import { getPatients, Patient } from '../api/patientApi';
import { useNavigate } from 'react-router-dom';

const PatientList: React.FC = () => {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [page, setPage] = useState<number>(0);
  const [rowsPerPage, setRowsPerPage] = useState<number>(15);
  const [totalElements, setTotalElements] = useState<number>(0);
  const navigate = useNavigate();

  useEffect(() => {
    fetchPatients();
  }, [page, rowsPerPage]);

  const fetchPatients = async () => {
    setLoading(true);
    try {
      const response = await getPatients(page, rowsPerPage);
      setPatients(response.content);
      setTotalElements(response.totalElements);
    } catch (error) {
      console.error('Error fetching patients:', error);
      // Handle error (e.g., show a toast notification)
    } finally {
      setLoading(false);
    }
  };

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0); // Reset to the first page when rows per page changes
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ padding: 3 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Patient List
      </Typography>
      <Button variant="contained" color="primary" onClick={() => navigate('/patients/new')} sx={{ mb: 2 }}>
        Register New Patient
      </Button>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>First Name</TableCell>
              <TableCell>Last Name</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Contact Number</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {patients.map((patient) => (
              <TableRow key={patient.id}>
                <TableCell>{patient.id}</TableCell>
                <TableCell>{patient.firstName}</TableCell>
                <TableCell>{patient.lastName}</TableCell>
                <TableCell>{patient.email}</TableCell>
                <TableCell>{patient.contactNumber}</TableCell>
                <TableCell>
                  <Button size="small" onClick={() => navigate(`/patients/${patient.id}`)}>
                    View
                  </Button>
                  <Button size="small" color="secondary" onClick={() => navigate(`/patients/${patient.id}/edit`)}>
                    Edit
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        component="div"
        count={totalElements}
        page={page}
        onPageChange={handleChangePage}
        rowsPerPage={rowsPerPage}
        onRowsPerPageChange={handleChangeRowsPerPage}
      />
    </Box>
  );
};

export default PatientList;

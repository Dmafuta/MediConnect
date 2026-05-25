import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  Box, Drawer, AppBar, Toolbar, Typography, List, ListItemButton,
  ListItemIcon, ListItemText, IconButton, Divider, Avatar, Menu,
  MenuItem, Tooltip, useTheme, useMediaQuery,
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import PeopleIcon from '@mui/icons-material/People';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import LocalHospitalIcon from '@mui/icons-material/LocalHospital';
import ScienceIcon from '@mui/icons-material/Science';
import MedicalServicesIcon from '@mui/icons-material/MedicalServices';
import BarChartIcon from '@mui/icons-material/BarChart';
import { useAuth } from '../context/AuthContext';

const DRAWER_WIDTH = 220;

const NAV_ITEMS = [
  { label: 'Patients',     icon: <PeopleIcon />,          path: '/patients' },
  { label: 'Appointments', icon: <CalendarMonthIcon />,   path: '/appointments' },
  { label: 'Visit Queue',  icon: <LocalHospitalIcon />,   path: '/queue' },
  { label: 'Lab Orders',   icon: <ScienceIcon />,         path: '/lab' },
  { label: 'Pharmacy',     icon: <MedicalServicesIcon />, path: '/pharmacy' },
  { label: 'Reports',      icon: <BarChartIcon />,        path: '/reports' },
];

export default function AppShell({ children }: { children: React.ReactNode }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));

  const [mobileOpen, setMobileOpen] = useState(false);
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const handleLogout = async () => {
    setAnchorEl(null);
    await logout();
    navigate('/login');
  };

  const drawer = (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      <Toolbar sx={{ bgcolor: 'primary.main' }}>
        <Typography variant="h6" sx={{ color: 'white', fontWeight: 700 }} noWrap>
          MediConnect
        </Typography>
      </Toolbar>
      <Divider />
      <List sx={{ flexGrow: 1, pt: 1 }}>
        {NAV_ITEMS.map(({ label, icon, path }) => {
          const active = location.pathname.startsWith(path);
          return (
            <ListItemButton
              key={path}
              selected={active}
              onClick={() => { navigate(path); if (isMobile) setMobileOpen(false); }}
              sx={{
                mx: 1, borderRadius: 1, mb: 0.5,
                '&.Mui-selected': { bgcolor: 'primary.light', color: 'primary.contrastText' },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36, color: active ? 'primary.main' : 'text.secondary' }}>
                {icon}
              </ListItemIcon>
              <ListItemText
                primary={label}
                slotProps={{ primary: { sx: { fontSize: 14 } } }}
              />
            </ListItemButton>
          );
        })}
      </List>
    </Box>
  );

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh' }}>
      <AppBar position="fixed" sx={{ zIndex: theme.zIndex.drawer + 1 }}>
        <Toolbar>
          {isMobile && (
            <IconButton color="inherit" edge="start" onClick={() => setMobileOpen(!mobileOpen)} sx={{ mr: 2 }}>
              <MenuIcon />
            </IconButton>
          )}
          <Typography variant="h6" sx={{ flexGrow: 1 }} noWrap>
            {NAV_ITEMS.find(n => location.pathname.startsWith(n.path))?.label ?? 'MediConnect EMR'}
          </Typography>
          <Tooltip title={user?.username ?? ''}>
            <IconButton onClick={(e) => setAnchorEl(e.currentTarget)} sx={{ p: 0 }}>
              <Avatar sx={{ bgcolor: 'secondary.main', width: 34, height: 34, fontSize: 14 }}>
                {user?.username?.[0]?.toUpperCase()}
              </Avatar>
            </IconButton>
          </Tooltip>
          <Menu anchorEl={anchorEl} open={!!anchorEl} onClose={() => setAnchorEl(null)}>
            <MenuItem disabled>
              <Typography variant="body2" color="text.secondary">{user?.username}</Typography>
            </MenuItem>
            <Divider />
            <MenuItem onClick={handleLogout}>Logout</MenuItem>
          </Menu>
        </Toolbar>
      </AppBar>

      <Box component="nav" sx={{ width: { md: DRAWER_WIDTH }, flexShrink: { md: 0 } }}>
        <Drawer
          variant={isMobile ? 'temporary' : 'permanent'}
          open={isMobile ? mobileOpen : true}
          onClose={() => setMobileOpen(false)}
          ModalProps={{ keepMounted: true }}
          sx={{
            '& .MuiDrawer-paper': { width: DRAWER_WIDTH, boxSizing: 'border-box' },
          }}
        >
          {drawer}
        </Drawer>
      </Box>

      <Box component="main" sx={{ flexGrow: 1, p: 3, mt: 8, width: { md: `calc(100% - ${DRAWER_WIDTH}px)` } }}>
        {children}
      </Box>
    </Box>
  );
}

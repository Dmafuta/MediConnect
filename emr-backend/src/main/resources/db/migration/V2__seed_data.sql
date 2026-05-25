-- MediConnect seed data
-- Reconstructed from DanpheEMR C# source (permissions, routes, applications)
-- Users are synthetic with BCrypt-hashed passwords
-- All inserts are idempotent (ON CONFLICT DO NOTHING)

-- ============================================================
-- 1. APPLICATIONS
-- ============================================================
INSERT INTO rbac_application (application_code, application_name, description, is_active, created_on)
VALUES
  ('ADT',     'ADT',            'Admission, Discharge and Transfer',      true, NOW()),
  ('ADTWARD', 'ADT Wards',      'Ward management under ADT',              true, NOW()),
  ('APT',     'Appointment',    'Outpatient appointment scheduling',       true, NOW()),
  ('BIL',     'Billing',        'Patient billing and payments',           true, NOW()),
  ('CLN',     'Clinical',       'Clinical notes and doctors workspace',   true, NOW()),
  ('DISP',    'Dispensary',     'Dispensary and OPD pharmacy',            true, NOW()),
  ('GOVT',    'Government',     'Government reporting module',            true, NOW()),
  ('LAB',     'Lab',            'Laboratory tests and results',           true, NOW()),
  ('NUR',     'Nursing',        'Nursing orders and inpatient care',      true, NOW()),
  ('OPD',     'OPD',            'Outpatient department',                  true, NOW()),
  ('PAT',     'Patient',        'Patient registration and search',        true, NOW()),
  ('PHRM',    'Pharmacy',       'Pharmacy dispensing and stock',          true, NOW()),
  ('RAD',     'Radiology',      'Radiology/Imaging orders and reports',   true, NOW()),
  ('RPT',     'Reports',        'System-wide reporting',                  true, NOW()),
  ('SETT',    'Settings',       'System configuration and settings',      true, NOW()),
  ('SYSADM',  'System Admin',   'Database backup and system admin tasks', true, NOW()),
  ('WARD',    'Ward Supply',    'Ward stock requisition and consumption',  true, NOW())
ON CONFLICT (application_code) DO NOTHING;

-- ============================================================
-- 2. PERMISSIONS  (grouped by application)
-- ============================================================

-- ADT
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('adt-view'),
  ('adt-admissionsearchpatient-view'),
  ('adt-admittedlist-view'),
  ('adt-createadmission-view'),
  ('adt-dischargedlist-view'),
  ('discharge-admission-button')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'ADT'
ON CONFLICT (permission_name) DO NOTHING;

-- Appointment
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('appointment-view'),
  ('appointment-createappointment-view'),
  ('appointment-listappointment-view'),
  ('appointment-listvisit-view'),
  ('appointment-patientsearch-view'),
  ('appointment-printsticker-view'),
  ('appointment-visit-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'APT'
ON CONFLICT (permission_name) DO NOTHING;

-- Billing
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('billing-view'),
  ('billing-billcancellationrequest-view'),
  ('billing-billorderrequest-view'),
  ('billing-billrequest-view'),
  ('billing-counteractivate-view'),
  ('billing-deposit-view'),
  ('billing-duplicatebillprint-view'),
  ('billing-editdoctor-view'),
  ('billing-receiptprint-view'),
  ('billing-searchpatient-view'),
  ('billing-settlements-bill-settlement-view'),
  ('billing-transaction-view'),
  ('billing-transactionitem-view'),
  ('billing-unpaidbills-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'BIL'
ON CONFLICT (permission_name) DO NOTHING;

-- Clinical / Doctors
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('clinical-scan-image-view'),
  ('clinical-patient-visit-note-view'),
  ('Clinical-notes-outpatExamination-view'),
  ('doctors-notes-view'),
  ('doctors-outpatientdoctor-view'),
  ('doctors-patientoverview-view'),
  ('doctors-patientoverviewmain-view'),
  ('doctors-patientvisithistory-view'),
  ('opd-summary-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'CLN'
ON CONFLICT (permission_name) DO NOTHING;

-- Government
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('government-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'GOVT'
ON CONFLICT (permission_name) DO NOTHING;

-- Lab
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('lab-settings-view'),
  ('reports-lab-itemwiselabreport-view'),
  ('reports-labmain-categorywiselabreport-view'),
  ('reports-labmain-totalrevenuefromlab-view'),
  ('reports-labmain-view'),
  ('reports-laboratoryservices-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'LAB'
ON CONFLICT (permission_name) DO NOTHING;

-- Nursing
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('nursing-order-list-view'),
  ('nursing-order-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'NUR'
ON CONFLICT (permission_name) DO NOTHING;

-- Patient
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('patient-view'),
  ('patient-register-address-view'),
  ('patient-register-guarantor-view'),
  ('patient-register-insurance-view'),
  ('patient-register-kinemergencycontact-view'),
  ('patient-register-view'),
  ('patient-searchpatient-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'PAT'
ON CONFLICT (permission_name) DO NOTHING;

-- Pharmacy
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('pharmacymain-view'),
  ('pharmacy-billingmain-view'),
  ('pharmacy-ordermain-view'),
  ('pharmacy-patient-view'),
  ('pharmacy-patientlist-view'),
  ('pharmacy-patientmain-view'),
  ('pharmacy-prescription-list-view'),
  ('pharmacy-prescription-view'),
  ('pharmacy-prescriptiongmain-view'),
  ('pharmacy-sale-list-view'),
  ('pharmacy-sale-return-view'),
  ('pharmacy-sale-view'),
  ('pharmacy-salemain-view'),
  ('pharmacy-settingmain-view'),
  ('pharmacy-stockmain-view'),
  ('pharmacy-suppliermanage-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'PHRM'
ON CONFLICT (permission_name) DO NOTHING;

-- Radiology
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('reports-radiologymain-categorywiseimagingreport-view'),
  ('reports-radiologymain-revenuegenerated-view'),
  ('reports-radiologymain-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'RAD'
ON CONFLICT (permission_name) DO NOTHING;

-- Reports
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('reports-view'),
  ('reports-admissionmain-diagnosiswisepatientreport-view'),
  ('reports-admissionmain-transferredpatient-view'),
  ('reports-admissionmain-view'),
  ('reports-appointmentmain-dailyappointmentreport-view'),
  ('reports-appointmentmain-departmentwiseappointmentreport-view'),
  ('reports-appointmentmain-districtwiseappointmentreport-view'),
  ('reports-appointmentmain-doctorwiseoutpatient-view'),
  ('reports-appointmentmain-phonebookappointmentreport-view'),
  ('reports-appointmentmain-view'),
  ('reports-billingmain-departmentsummaryreport-view'),
  ('reports-billingmain-dischargedpatient-view'),
  ('reports-billingmain-patientbillhistory-view'),
  ('reports-billingmain-patientcensusreport-view'),
  ('reports-billingmain-patientcreditsummary-view'),
  ('reports-billingmain-totaladmittedpatient-view'),
  ('reports-billingmain-view'),
  ('reports-doctorsmain-doctorwiseencounterpatientreport-view'),
  ('reports-doctorsmain-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'RPT'
ON CONFLICT (permission_name) DO NOTHING;

-- Settings
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('settings-view'),
  ('settings-adtmanage-view'),
  ('settings-clinicalmanage-view'),
  ('settings-departmentsmanage-view'),
  ('settings-employeemanage-view'),
  ('settings-geolocationmanage-view'),
  ('settings-radiologymanage-view'),
  ('ssettings-securitymanage-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'SETT'
ON CONFLICT (permission_name) DO NOTHING;

-- System Admin
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('systemadmin-view'),
  ('systemadmin-databasebackup-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'SYSADM'
ON CONFLICT (permission_name) DO NOTHING;

-- Ward Supply
INSERT INTO rbac_permission (permission_name, application_id, is_active, created_on)
SELECT v.perm, a.application_id, true, NOW()
FROM (VALUES
  ('wardsupply-consumption-view'),
  ('wardsupply-requisition-view'),
  ('wardsupply-stock-view')
) AS v(perm)
JOIN rbac_application a ON a.application_code = 'WARD'
ON CONFLICT (permission_name) DO NOTHING;

-- ============================================================
-- 3. ROLES  (default_route_id set after routes are inserted)
-- ============================================================
INSERT INTO rbac_role (role_name, role_description, role_type, application_id, is_sys_admin, is_active, role_priority, created_on)
VALUES
  ('System Admin',   'Full system access',               'SystemAdmin', NULL, true,  true, 1, NOW()),
  ('Doctor',         'Clinical staff — doctors',         'Custom',
   (SELECT application_id FROM rbac_application WHERE application_code = 'CLN'),
   false, true, 2, NOW()),
  ('Nurse',          'Nursing care and orders',          'Custom',
   (SELECT application_id FROM rbac_application WHERE application_code = 'NUR'),
   false, true, 3, NOW()),
  ('Billing Clerk',  'Billing and payment processing',   'Custom',
   (SELECT application_id FROM rbac_application WHERE application_code = 'BIL'),
   false, true, 4, NOW()),
  ('Lab Technician', 'Laboratory tests and reporting',   'Custom',
   (SELECT application_id FROM rbac_application WHERE application_code = 'LAB'),
   false, true, 5, NOW()),
  ('Pharmacist',     'Pharmacy dispensing and stock',    'Custom',
   (SELECT application_id FROM rbac_application WHERE application_code = 'PHRM'),
   false, true, 6, NOW()),
  ('Receptionist',   'Front desk — appointments and patient registration', 'Custom',
   (SELECT application_id FROM rbac_application WHERE application_code = 'APT'),
   false, true, 7, NOW())
ON CONFLICT (role_name) DO NOTHING;

-- ============================================================
-- 4. ROUTES — parent routes first (no parent_route_id)
-- ============================================================
INSERT INTO rbac_route_config (url_full_path, display_name, permission_id, parent_route_id, default_show, router_link, is_active, is_secondary_nav_in_dropdown, display_seq)
SELECT v.path, v.name, p.permission_id, NULL, true, v.link, true, false, v.seq
FROM (VALUES
  ('Patient',      'Patient',      'patient-view',                   'Patient',      1),
  ('ADTMain',      'ADT',          'adt-view',                       'ADTMain',      2),
  ('Appointment',  'Appointment',  'appointment-view',               'Appointment',  3),
  ('Doctors',      'Doctors',      'doctors-patientoverviewmain-view','Doctors',      4),
  ('Nursing',      'Nursing',      'nursing-order-view',             'Nursing',      5),
  ('Lab',          'Lab',          'lab-settings-view',              'Lab',          6),
  ('Pharmacy',     'Pharmacy',     'pharmacymain-view',              'Pharmacy',     7),
  ('Billing',      'Billing',      'billing-view',                   'Billing',      8),
  ('Reports',      'Reports',      'reports-view',                   'Reports',      9),
  ('Settings',     'Settings',     'settings-view',                  'Settings',    10),
  ('SystemAdmin',  'System Admin', 'systemadmin-view',               'SystemAdmin', 11)
) AS v(path, name, perm, link, seq)
JOIN rbac_permission p ON p.permission_name = v.perm
WHERE NOT EXISTS (
  SELECT 1 FROM rbac_route_config r WHERE r.url_full_path = v.path AND r.parent_route_id IS NULL
);

-- ============================================================
-- 5. ROUTES — child routes
-- ============================================================
INSERT INTO rbac_route_config (url_full_path, display_name, permission_id, parent_route_id, default_show, router_link, is_active, is_secondary_nav_in_dropdown, display_seq)
SELECT v.path, v.name, p.permission_id, parent.route_id, v.show, v.link, true, false, v.seq
FROM (VALUES
  -- Patient children
  ('Patient/SearchPatient',   'Search Patient',  'patient-searchpatient-view',   'Patient',  true,  'SearchPatient', 1),
  ('Patient/Register',        'Register Patient','patient-register-view',        'Patient',  true,  'Register',      2),
  -- ADT children
  ('ADTMain/AdmittedList',    'Admitted List',   'adt-admittedlist-view',        'ADTMain',  true,  'AdmittedList',  1),
  ('ADTMain/CreateAdmission', 'New Admission',   'adt-createadmission-view',     'ADTMain',  true,  'CreateAdmission',2),
  ('ADTMain/DischargedList',  'Discharged List', 'adt-dischargedlist-view',      'ADTMain',  false, 'DischargedList', 3),
  -- Appointment children
  ('Appointment/ListVisit',       'Visit List',        'appointment-listvisit-view',       'Appointment', true,  'ListVisit',       1),
  ('Appointment/ListAppointment', 'Appointment List',  'appointment-listappointment-view', 'Appointment', true,  'ListAppointment', 2),
  ('Appointment/PatientSearch',   'Patient Search',    'appointment-patientsearch-view',   'Appointment', false, 'PatientSearch',   3),
  -- Doctors/Clinical children
  ('Doctors/PatientOverviewMain',  'Patient Overview',  'doctors-patientoverviewmain-view',  'Doctors', true,  'PatientOverviewMain', 1),
  ('Doctors/OutpatientDoctor',     'OPD Doctor',        'doctors-outpatientdoctor-view',     'Doctors', false, 'OutpatientDoctor',    2),
  -- Nursing children
  ('Nursing/InPatient',            'Inpatient',         'nursing-order-view',      'Nursing', true,  'InPatient',          1),
  ('Nursing/Orders/NursingOrderList','Nursing Orders',  'nursing-order-list-view', 'Nursing', false, 'NursingOrderList',   2),
  -- Billing children
  ('Billing/SearchPatient',       'Search Patient',     'billing-searchpatient-view',          'Billing', true,  'SearchPatient',  1),
  ('Billing/Transaction',         'Transaction',        'billing-transaction-view',            'Billing', true,  'Transaction',    2),
  ('Billing/CounterActivate',     'Counter Activate',   'billing-counteractivate-view',        'Billing', false, 'CounterActivate',3),
  ('Billing/ReceiptPrint',        'Receipt Print',      'billing-receiptprint-view',           'Billing', false, 'ReceiptPrint',   4),
  -- Pharmacy children
  ('Pharmacy/PatientMain',        'Dispense',           'pharmacy-patientmain-view',           'Pharmacy', true,  'PatientMain',   1),
  ('Pharmacy/StockMain',          'Stock',              'pharmacy-stockmain-view',             'Pharmacy', true,  'StockMain',     2),
  ('Pharmacy/OrderMain',          'Orders',             'pharmacy-ordermain-view',             'Pharmacy', false, 'OrderMain',     3),
  ('Pharmacy/Setting',            'Settings',           'pharmacy-settingmain-view',           'Pharmacy', false, 'Setting',       4),
  -- Reports children
  ('Reports/Billing',             'Billing Reports',    'reports-billingmain-view',            'Reports', true,  'Billing',        1),
  ('Reports/ADT',                 'ADT Reports',        'reports-admissionmain-view',          'Reports', false, 'ADT',            2),
  ('Reports/Appointment',         'Appointment Reports','reports-appointmentmain-view',        'Reports', false, 'Appointment',    3),
  ('Reports/Lab',                 'Lab Reports',        'reports-labmain-view',                'Reports', false, 'Lab',            4),
  ('Reports/Doctors',             'Doctor Reports',     'reports-doctorsmain-view',            'Reports', false, 'Doctors',        5),
  ('Reports/Radiology',           'Radiology Reports',  'reports-radiologymain-view',          'Reports', false, 'Radiology',      6),
  -- Settings children
  ('Settings/SecurityManage',     'Security',           'ssettings-securitymanage-view',       'Settings', true,  'SecurityManage',  1),
  ('Settings/ADTManage',          'ADT Config',         'settings-adtmanage-view',             'Settings', false, 'ADTManage',       2),
  ('Settings/DeptManage',         'Departments',        'settings-departmentsmanage-view',     'Settings', false, 'DeptManage',      3),
  ('Settings/EmployeeManage',     'Employees',          'settings-employeemanage-view',        'Settings', false, 'EmployeeManage',  4),
  -- System Admin children
  ('SystemAdmin/DatabaseBackup',  'Database Backup',    'systemadmin-databasebackup-view',     'SystemAdmin', true, 'DatabaseBackup', 1)
) AS v(path, name, perm, parent_path, show, link, seq)
JOIN rbac_permission p ON p.permission_name = v.perm
JOIN rbac_route_config parent ON parent.url_full_path = v.parent_path AND parent.parent_route_id IS NULL
WHERE NOT EXISTS (
  SELECT 1 FROM rbac_route_config r WHERE r.url_full_path = v.path
);

-- ============================================================
-- 6. UPDATE ROLES with default landing route
-- ============================================================
UPDATE rbac_role SET default_route_id = (SELECT route_id FROM rbac_route_config WHERE url_full_path = 'Patient/SearchPatient')
WHERE role_name = 'Receptionist' AND default_route_id IS NULL;

UPDATE rbac_role SET default_route_id = (SELECT route_id FROM rbac_route_config WHERE url_full_path = 'Doctors/PatientOverviewMain')
WHERE role_name = 'Doctor' AND default_route_id IS NULL;

UPDATE rbac_role SET default_route_id = (SELECT route_id FROM rbac_route_config WHERE url_full_path = 'Nursing/InPatient')
WHERE role_name = 'Nurse' AND default_route_id IS NULL;

UPDATE rbac_role SET default_route_id = (SELECT route_id FROM rbac_route_config WHERE url_full_path = 'Billing/SearchPatient')
WHERE role_name = 'Billing Clerk' AND default_route_id IS NULL;

UPDATE rbac_role SET default_route_id = (SELECT route_id FROM rbac_route_config WHERE url_full_path = 'Lab')
WHERE role_name = 'Lab Technician' AND default_route_id IS NULL;

UPDATE rbac_role SET default_route_id = (SELECT route_id FROM rbac_route_config WHERE url_full_path = 'Pharmacy/PatientMain')
WHERE role_name = 'Pharmacist' AND default_route_id IS NULL;

UPDATE rbac_role SET default_route_id = (SELECT route_id FROM rbac_route_config WHERE url_full_path = 'Settings/SecurityManage')
WHERE role_name = 'System Admin' AND default_route_id IS NULL;

-- ============================================================
-- 7. USERS  (synthetic — passwords are BCrypt of stated value)
-- Passwords: admin→Admin@123  doctor→Doctor@123  nurse→Nurse@123
--            billing→Billing@123  lab→Lab@123
-- ============================================================
INSERT INTO rbac_user (user_name, password, email, is_active, needs_password_update, created_on, landing_page_route_id)
SELECT 'admin', '$2b$10$ekW48gZ/PDP5GVjWl6Umae0OnASg.l7AOy3MOY.Vdp0cjfxCvo2xa',
       'admin@mediconnect.local', true, false, NOW(),
       (SELECT route_id FROM rbac_route_config WHERE url_full_path = 'Settings/SecurityManage')
WHERE NOT EXISTS (SELECT 1 FROM rbac_user WHERE user_name = 'admin');

INSERT INTO rbac_user (user_name, password, email, is_active, needs_password_update, created_on, landing_page_route_id)
SELECT 'doctor', '$2b$10$hmTAFZE/LoB.pZ6FhT91AO5Bgzb2z3G/FUuiLQyFuc7pvvBHImVGe',
       'doctor@mediconnect.local', true, false, NOW(),
       (SELECT route_id FROM rbac_route_config WHERE url_full_path = 'Doctors/PatientOverviewMain')
WHERE NOT EXISTS (SELECT 1 FROM rbac_user WHERE user_name = 'doctor');

INSERT INTO rbac_user (user_name, password, email, is_active, needs_password_update, created_on, landing_page_route_id)
SELECT 'nurse', '$2b$10$Sp1aC34mdV0SRMoaJWZOhOFJBcutEeu1Tcs8STQtmo7CqXWP6bXkm',
       'nurse@mediconnect.local', true, false, NOW(),
       (SELECT route_id FROM rbac_route_config WHERE url_full_path = 'Nursing/InPatient')
WHERE NOT EXISTS (SELECT 1 FROM rbac_user WHERE user_name = 'nurse');

INSERT INTO rbac_user (user_name, password, email, is_active, needs_password_update, created_on, landing_page_route_id)
SELECT 'billing', '$2b$10$yiLI6vV6aYTs3TTFBEyp3eWAZEMLkRUvOmF9xqG.mItX.MjelwFe2',
       'billing@mediconnect.local', true, false, NOW(),
       (SELECT route_id FROM rbac_route_config WHERE url_full_path = 'Billing/SearchPatient')
WHERE NOT EXISTS (SELECT 1 FROM rbac_user WHERE user_name = 'billing');

INSERT INTO rbac_user (user_name, password, email, is_active, needs_password_update, created_on, landing_page_route_id)
SELECT 'lab', '$2b$10$VACDv268G5OCY9AErlSAiOjGFg5OPCohMgii0ux8j33YpKMdeY.YW',
       'lab@mediconnect.local', true, false, NOW(),
       (SELECT route_id FROM rbac_route_config WHERE url_full_path = 'Lab')
WHERE NOT EXISTS (SELECT 1 FROM rbac_user WHERE user_name = 'lab');

-- ============================================================
-- 8. ROLE-PERMISSION MAPPINGS
-- ============================================================

-- System Admin gets every permission
INSERT INTO rbac_map_role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM rbac_role r, rbac_permission p
WHERE r.role_name = 'System Admin'
ON CONFLICT DO NOTHING;

-- Doctor
INSERT INTO rbac_map_role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM rbac_role r
JOIN rbac_permission p ON p.permission_name IN (
  'doctors-notes-view','doctors-outpatientdoctor-view','doctors-patientoverview-view',
  'doctors-patientoverviewmain-view','doctors-patientvisithistory-view',
  'clinical-scan-image-view','clinical-patient-visit-note-view',
  'Clinical-notes-outpatExamination-view','opd-summary-view',
  'patient-view','patient-searchpatient-view','patient-register-view',
  'nursing-order-view','nursing-order-list-view','lab-settings-view',
  'reports-doctorsmain-view','reports-doctorsmain-doctorwiseencounterpatientreport-view',
  'reports-labmain-view','appointment-listvisit-view','appointment-patientsearch-view'
)
WHERE r.role_name = 'Doctor'
ON CONFLICT DO NOTHING;

-- Nurse
INSERT INTO rbac_map_role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM rbac_role r
JOIN rbac_permission p ON p.permission_name IN (
  'nursing-order-view','nursing-order-list-view',
  'patient-view','patient-searchpatient-view',
  'clinical-scan-image-view',
  'doctors-patientoverview-view','doctors-patientoverviewmain-view',
  'adt-admittedlist-view'
)
WHERE r.role_name = 'Nurse'
ON CONFLICT DO NOTHING;

-- Billing Clerk
INSERT INTO rbac_map_role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM rbac_role r
JOIN rbac_permission p ON p.permission_name IN (
  'billing-view','billing-billcancellationrequest-view','billing-billorderrequest-view',
  'billing-billrequest-view','billing-counteractivate-view','billing-deposit-view',
  'billing-duplicatebillprint-view','billing-editdoctor-view','billing-receiptprint-view',
  'billing-searchpatient-view','billing-settlements-bill-settlement-view',
  'billing-transaction-view','billing-transactionitem-view','billing-unpaidbills-view',
  'patient-view','patient-searchpatient-view',
  'appointment-listappointment-view','appointment-listvisit-view',
  'reports-billingmain-view','reports-billingmain-departmentsummaryreport-view',
  'reports-billingmain-dischargedpatient-view','reports-billingmain-patientbillhistory-view',
  'reports-billingmain-patientcensusreport-view','reports-billingmain-patientcreditsummary-view',
  'reports-billingmain-totaladmittedpatient-view'
)
WHERE r.role_name = 'Billing Clerk'
ON CONFLICT DO NOTHING;

-- Lab Technician
INSERT INTO rbac_map_role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM rbac_role r
JOIN rbac_permission p ON p.permission_name IN (
  'lab-settings-view',
  'patient-view','patient-searchpatient-view',
  'reports-labmain-view','reports-labmain-categorywiselabreport-view',
  'reports-labmain-totalrevenuefromlab-view','reports-lab-itemwiselabreport-view',
  'reports-laboratoryservices-view'
)
WHERE r.role_name = 'Lab Technician'
ON CONFLICT DO NOTHING;

-- Pharmacist
INSERT INTO rbac_map_role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM rbac_role r
JOIN rbac_permission p ON p.permission_name IN (
  'pharmacymain-view','pharmacy-billingmain-view','pharmacy-ordermain-view',
  'pharmacy-patient-view','pharmacy-patientlist-view','pharmacy-patientmain-view',
  'pharmacy-prescription-list-view','pharmacy-prescription-view','pharmacy-prescriptiongmain-view',
  'pharmacy-sale-list-view','pharmacy-sale-return-view','pharmacy-sale-view',
  'pharmacy-salemain-view','pharmacy-settingmain-view','pharmacy-stockmain-view',
  'pharmacy-suppliermanage-view',
  'patient-view','patient-searchpatient-view'
)
WHERE r.role_name = 'Pharmacist'
ON CONFLICT DO NOTHING;

-- Receptionist
INSERT INTO rbac_map_role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM rbac_role r
JOIN rbac_permission p ON p.permission_name IN (
  'appointment-view','appointment-createappointment-view','appointment-listappointment-view',
  'appointment-listvisit-view','appointment-patientsearch-view',
  'appointment-printsticker-view','appointment-visit-view',
  'patient-view','patient-register-address-view','patient-register-guarantor-view',
  'patient-register-insurance-view','patient-register-kinemergencycontact-view',
  'patient-register-view','patient-searchpatient-view',
  'billing-view','billing-searchpatient-view','billing-billrequest-view'
)
WHERE r.role_name = 'Receptionist'
ON CONFLICT DO NOTHING;

-- ============================================================
-- 9. USER-ROLE MAPPINGS
-- ============================================================
INSERT INTO rbac_map_user_role (user_id, role_id)
SELECT u.user_id, r.role_id FROM rbac_user u, rbac_role r
WHERE u.user_name = 'admin'   AND r.role_name = 'System Admin'
ON CONFLICT DO NOTHING;

INSERT INTO rbac_map_user_role (user_id, role_id)
SELECT u.user_id, r.role_id FROM rbac_user u, rbac_role r
WHERE u.user_name = 'doctor'  AND r.role_name = 'Doctor'
ON CONFLICT DO NOTHING;

INSERT INTO rbac_map_user_role (user_id, role_id)
SELECT u.user_id, r.role_id FROM rbac_user u, rbac_role r
WHERE u.user_name = 'nurse'   AND r.role_name = 'Nurse'
ON CONFLICT DO NOTHING;

INSERT INTO rbac_map_user_role (user_id, role_id)
SELECT u.user_id, r.role_id FROM rbac_user u, rbac_role r
WHERE u.user_name = 'billing' AND r.role_name = 'Billing Clerk'
ON CONFLICT DO NOTHING;

INSERT INTO rbac_map_user_role (user_id, role_id)
SELECT u.user_id, r.role_id FROM rbac_user u, rbac_role r
WHERE u.user_name = 'lab'     AND r.role_name = 'Lab Technician'
ON CONFLICT DO NOTHING;

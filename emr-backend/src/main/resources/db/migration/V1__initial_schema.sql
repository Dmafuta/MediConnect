-- MediConnect EMR — initial schema
-- Tables are created in FK dependency order.
-- Uses IF NOT EXISTS so this is safe to apply over an existing schema.

-- ──────────────────────────────────────────────
-- RBAC / Security
-- ──────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS rbac_application (
    application_id   BIGSERIAL PRIMARY KEY,
    application_code VARCHAR(255) UNIQUE NOT NULL,
    application_name VARCHAR(255) NOT NULL,
    description      TEXT,
    is_active        BOOLEAN NOT NULL DEFAULT TRUE,
    created_by       VARCHAR(255),
    created_on       TIMESTAMP,
    modified_by      VARCHAR(255),
    modified_on      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rbac_permission (
    permission_id   BIGSERIAL PRIMARY KEY,
    permission_name VARCHAR(255) UNIQUE NOT NULL,
    description     TEXT,
    application_id  BIGINT NOT NULL REFERENCES rbac_application(application_id),
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_by      VARCHAR(255),
    created_on      TIMESTAMP,
    modified_by     VARCHAR(255),
    modified_on     TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rbac_route_config (
    route_id                     BIGSERIAL PRIMARY KEY,
    url_full_path                VARCHAR(255),
    display_name                 VARCHAR(255),
    permission_id                BIGINT REFERENCES rbac_permission(permission_id),
    parent_route_id              BIGINT REFERENCES rbac_route_config(route_id),
    default_show                 BOOLEAN,
    router_link                  VARCHAR(255),
    is_active                    BOOLEAN,
    is_secondary_nav_in_dropdown BOOLEAN,
    css                          VARCHAR(255),
    display_seq                  INTEGER
);

CREATE TABLE IF NOT EXISTS rbac_role (
    role_id          BIGSERIAL PRIMARY KEY,
    role_name        VARCHAR(255) UNIQUE NOT NULL,
    role_description TEXT,
    role_type        VARCHAR(255),
    application_id   BIGINT REFERENCES rbac_application(application_id),
    is_sys_admin     BOOLEAN NOT NULL DEFAULT FALSE,
    is_active        BOOLEAN NOT NULL DEFAULT TRUE,
    role_priority    INTEGER,
    default_route_id BIGINT REFERENCES rbac_route_config(route_id),
    created_by       VARCHAR(255),
    created_on       TIMESTAMP,
    modified_by      VARCHAR(255),
    modified_on      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rbac_user (
    user_id               BIGSERIAL PRIMARY KEY,
    employee_id           BIGINT,
    user_name             VARCHAR(255) UNIQUE NOT NULL,
    password              VARCHAR(255) NOT NULL,
    email                 VARCHAR(255),
    created_by            VARCHAR(255),
    created_on            TIMESTAMP,
    modified_by           VARCHAR(255),
    modified_on           TIMESTAMP,
    is_active             BOOLEAN NOT NULL DEFAULT TRUE,
    needs_password_update BOOLEAN DEFAULT TRUE,
    landing_page_route_id BIGINT REFERENCES rbac_route_config(route_id)
);

CREATE TABLE IF NOT EXISTS rbac_map_user_role (
    user_id BIGINT NOT NULL REFERENCES rbac_user(user_id),
    role_id BIGINT NOT NULL REFERENCES rbac_role(role_id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS rbac_map_role_permission (
    role_id       BIGINT NOT NULL REFERENCES rbac_role(role_id),
    permission_id BIGINT NOT NULL REFERENCES rbac_permission(permission_id),
    PRIMARY KEY (role_id, permission_id)
);

-- ──────────────────────────────────────────────
-- Patient
-- ──────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS patients (
    id                         BIGSERIAL PRIMARY KEY,
    mrn                        VARCHAR(255) UNIQUE,
    salutation                 VARCHAR(255),
    first_name                 VARCHAR(255) NOT NULL,
    middle_name                VARCHAR(255),
    last_name                  VARCHAR(255) NOT NULL,
    date_of_birth              DATE,
    gender                     VARCHAR(255) NOT NULL,
    blood_group                VARCHAR(255),
    marital_status             VARCHAR(255),
    contact_number             VARCHAR(255) UNIQUE,
    alternate_phone            VARCHAR(255),
    email                      VARCHAR(255) UNIQUE,
    address                    TEXT,
    emergency_contact_name     VARCHAR(255),
    emergency_contact_relation VARCHAR(255),
    emergency_contact_number   VARCHAR(255),
    is_active                  BOOLEAN NOT NULL DEFAULT TRUE,
    created_by                 VARCHAR(255),
    created_on                 TIMESTAMP,
    modified_by                VARCHAR(255),
    modified_on                TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patient_allergies (
    id            BIGSERIAL PRIMARY KEY,
    patient_id    BIGINT NOT NULL REFERENCES patients(id),
    allergen_name VARCHAR(255) NOT NULL,
    allergy_type  VARCHAR(50),
    severity      VARCHAR(50),
    verified      BOOLEAN DEFAULT FALSE,
    reaction      VARCHAR(255),
    comments      TEXT,
    is_active     BOOLEAN DEFAULT TRUE,
    created_by    VARCHAR(255),
    created_on    TIMESTAMP,
    modified_by   VARCHAR(255),
    modified_on   TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patient_problems (
    id                  BIGSERIAL PRIMARY KEY,
    patient_id          BIGINT NOT NULL REFERENCES patients(id),
    problem_description TEXT NOT NULL,
    current_status      VARCHAR(50) DEFAULT 'ACTIVE',
    note                TEXT,
    onset_date          DATE,
    resolved_date       DATE,
    is_resolved         BOOLEAN DEFAULT FALSE,
    is_principal_problem BOOLEAN DEFAULT FALSE,
    created_by          VARCHAR(255),
    created_on          TIMESTAMP,
    modified_by         VARCHAR(255),
    modified_on         TIMESTAMP
);

-- ──────────────────────────────────────────────
-- Appointment
-- ──────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS appointments (
    id                   BIGSERIAL PRIMARY KEY,
    patient_id           BIGINT REFERENCES patients(id),
    walkin_first_name    VARCHAR(255),
    walkin_last_name     VARCHAR(255),
    walkin_contact_number VARCHAR(255),
    walkin_gender        VARCHAR(255),
    appointment_date     DATE NOT NULL,
    appointment_time     TIME,
    provider_id          BIGINT NOT NULL REFERENCES rbac_user(user_id),
    department_name      VARCHAR(255),
    appointment_type     VARCHAR(255),
    appointment_status   VARCHAR(50) NOT NULL DEFAULT 'BOOKED',
    reason               TEXT,
    cancelled_on         TIMESTAMP,
    cancelled_by         VARCHAR(255),
    cancelled_remarks    TEXT,
    created_by           VARCHAR(255),
    created_on           TIMESTAMP,
    modified_by          VARCHAR(255),
    modified_on          TIMESTAMP
);

-- ──────────────────────────────────────────────
-- Clinical / Visit
-- ──────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS patient_visits (
    id                 BIGSERIAL PRIMARY KEY,
    visit_code         VARCHAR(255) UNIQUE,
    patient_id         BIGINT NOT NULL REFERENCES patients(id),
    appointment_id     BIGINT REFERENCES appointments(id),
    visit_date         DATE NOT NULL,
    visit_time         TIME,
    provider_id        BIGINT NOT NULL REFERENCES rbac_user(user_id),
    department_name    VARCHAR(255),
    visit_type         VARCHAR(50),
    visit_status       VARCHAR(50) NOT NULL DEFAULT 'QUEUED',
    queue_status       VARCHAR(50) DEFAULT 'WAITING',
    billing_status     VARCHAR(50) DEFAULT 'PENDING',
    is_triaged         BOOLEAN DEFAULT FALSE,
    is_visit_continued BOOLEAN DEFAULT FALSE,
    parent_visit_id    BIGINT REFERENCES patient_visits(id),
    created_by         VARCHAR(255),
    created_on         TIMESTAMP,
    modified_by        VARCHAR(255),
    modified_on        TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patient_vitals (
    id               BIGSERIAL PRIMARY KEY,
    visit_id         BIGINT NOT NULL REFERENCES patient_visits(id),
    height           DOUBLE PRECISION,
    height_unit      VARCHAR(10),
    weight           DOUBLE PRECISION,
    weight_unit      VARCHAR(10),
    bmi              DOUBLE PRECISION,
    temperature      DOUBLE PRECISION,
    temperature_unit VARCHAR(5),
    pulse            INTEGER,
    bp_systolic      INTEGER,
    bp_diastolic     INTEGER,
    respiratory_rate INTEGER,
    sp_o2            DOUBLE PRECISION,
    pain_scale       INTEGER,
    free_notes       TEXT,
    vitals_taken_on  TIMESTAMP,
    created_by       VARCHAR(255),
    created_on       TIMESTAMP,
    modified_by      VARCHAR(255),
    modified_on      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patient_diagnoses (
    id               BIGSERIAL PRIMARY KEY,
    patient_id       BIGINT NOT NULL REFERENCES patients(id),
    visit_id         BIGINT NOT NULL REFERENCES patient_visits(id),
    icd10_code       VARCHAR(50),
    icd10_description VARCHAR(255) NOT NULL,
    diagnosis_type   VARCHAR(50),
    is_active        BOOLEAN DEFAULT TRUE,
    created_by       VARCHAR(255),
    created_on       TIMESTAMP,
    modified_by      VARCHAR(255),
    modified_on      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS clinical_notes (
    id           BIGSERIAL PRIMARY KEY,
    visit_id     BIGINT NOT NULL REFERENCES patient_visits(id),
    author_id    BIGINT NOT NULL REFERENCES rbac_user(user_id),
    note_type    VARCHAR(50) NOT NULL DEFAULT 'SOAP',
    subjective   TEXT,
    objective    TEXT,
    assessment   TEXT,
    plan         TEXT,
    note_content TEXT,
    is_finalized BOOLEAN DEFAULT FALSE,
    finalized_on TIMESTAMP,
    created_by   VARCHAR(255),
    created_on   TIMESTAMP,
    modified_by  VARCHAR(255),
    modified_on  TIMESTAMP
);

CREATE TABLE IF NOT EXISTS note_templates (
    id                   BIGSERIAL PRIMARY KEY,
    template_name        VARCHAR(255) UNIQUE NOT NULL,
    visit_type           VARCHAR(255),
    note_type            VARCHAR(50) NOT NULL DEFAULT 'SOAP',
    subjective_template  TEXT,
    objective_template   TEXT,
    assessment_template  TEXT,
    plan_template        TEXT,
    content_template     TEXT,
    is_active            BOOLEAN NOT NULL DEFAULT TRUE,
    created_by           VARCHAR(255),
    created_on           TIMESTAMP,
    modified_by          VARCHAR(255),
    modified_on          TIMESTAMP
);

CREATE TABLE IF NOT EXISTS referral_orders (
    id                   BIGSERIAL PRIMARY KEY,
    patient_id           BIGINT NOT NULL REFERENCES patients(id),
    visit_id             BIGINT NOT NULL REFERENCES patient_visits(id),
    referred_by_id       BIGINT NOT NULL REFERENCES rbac_user(user_id),
    referred_to_specialty VARCHAR(255) NOT NULL,
    referred_to_provider VARCHAR(255),
    priority             VARCHAR(50) NOT NULL DEFAULT 'ROUTINE',
    status               VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    reason_for_referral  TEXT,
    notes                TEXT,
    created_by           VARCHAR(255),
    created_on           TIMESTAMP,
    modified_by          VARCHAR(255),
    modified_on          TIMESTAMP
);

-- ──────────────────────────────────────────────
-- Emergency
-- ──────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS triage_assessments (
    id                        BIGSERIAL PRIMARY KEY,
    visit_id                  BIGINT NOT NULL UNIQUE REFERENCES patient_visits(id),
    esi_level                 INTEGER,
    chief_complaint           TEXT NOT NULL,
    history_of_present_illness TEXT,
    onset_duration            VARCHAR(255),
    onset_character           VARCHAR(255),
    general_appearance        VARCHAR(255),
    mental_status             VARCHAR(255),
    disposition               VARCHAR(50) DEFAULT 'WAITING_ROOM',
    triage_completed_at       TIMESTAMP,
    additional_notes          TEXT,
    created_by                VARCHAR(255),
    created_on                TIMESTAMP,
    modified_by               VARCHAR(255),
    modified_on               TIMESTAMP
);

-- ──────────────────────────────────────────────
-- Lab
-- ──────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS lab_orders (
    id                   BIGSERIAL PRIMARY KEY,
    patient_id           BIGINT NOT NULL REFERENCES patients(id),
    visit_id             BIGINT NOT NULL REFERENCES patient_visits(id),
    ordered_by_id        BIGINT NOT NULL REFERENCES rbac_user(user_id),
    test_name            VARCHAR(255) NOT NULL,
    test_code            VARCHAR(255),
    priority             VARCHAR(50) NOT NULL DEFAULT 'ROUTINE',
    status               VARCHAR(50) NOT NULL DEFAULT 'ORDERED',
    clinical_indication  TEXT,
    result_value         TEXT,
    result_unit          VARCHAR(255),
    result_range         VARCHAR(255),
    result_interpretation VARCHAR(255),
    result_date          TIMESTAMP,
    notes                TEXT,
    created_by           VARCHAR(255),
    created_on           TIMESTAMP,
    modified_by          VARCHAR(255),
    modified_on          TIMESTAMP
);

-- ──────────────────────────────────────────────
-- Radiology
-- ──────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS imaging_orders (
    id                  BIGSERIAL PRIMARY KEY,
    patient_id          BIGINT NOT NULL REFERENCES patients(id),
    visit_id            BIGINT NOT NULL REFERENCES patient_visits(id),
    ordered_by_id       BIGINT NOT NULL REFERENCES rbac_user(user_id),
    study_type          VARCHAR(50) NOT NULL,
    body_part           VARCHAR(255),
    priority            VARCHAR(50) NOT NULL DEFAULT 'ROUTINE',
    status              VARCHAR(50) NOT NULL DEFAULT 'ORDERED',
    clinical_indication TEXT,
    report              TEXT,
    report_date         TIMESTAMP,
    notes               TEXT,
    created_by          VARCHAR(255),
    created_on          TIMESTAMP,
    modified_by         VARCHAR(255),
    modified_on         TIMESTAMP
);

-- ──────────────────────────────────────────────
-- Pharmacy
-- ──────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS medication_prescriptions (
    id                  BIGSERIAL PRIMARY KEY,
    patient_id          BIGINT NOT NULL REFERENCES patients(id),
    prescribed_by_id    BIGINT NOT NULL REFERENCES rbac_user(user_id),
    visit_id            BIGINT REFERENCES patient_visits(id),
    medication_name     VARCHAR(255) NOT NULL,
    route               VARCHAR(255),
    dose                VARCHAR(255) NOT NULL,
    frequency           VARCHAR(255) NOT NULL,
    duration            INTEGER,
    duration_type       VARCHAR(50),
    refill              INTEGER,
    type_of_medication  VARCHAR(255),
    instructions        TEXT,
    is_active           BOOLEAN DEFAULT TRUE,
    created_by          VARCHAR(255),
    created_on          TIMESTAMP,
    modified_by         VARCHAR(255),
    modified_on         TIMESTAMP
);

-- ──────────────────────────────────────────────
-- Nursing / MAR
-- ──────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS medication_administrations (
    id                 BIGSERIAL PRIMARY KEY,
    patient_id         BIGINT NOT NULL REFERENCES patients(id),
    visit_id           BIGINT REFERENCES patient_visits(id),
    prescription_id    BIGINT REFERENCES medication_prescriptions(id),
    administered_by_id BIGINT NOT NULL REFERENCES rbac_user(user_id),
    medication_name    VARCHAR(255) NOT NULL,
    dose               VARCHAR(255) NOT NULL,
    route              VARCHAR(255),
    administered_at    TIMESTAMP NOT NULL,
    status             VARCHAR(50) NOT NULL DEFAULT 'ADMINISTERED',
    hold_reason        VARCHAR(255),
    notes              TEXT,
    created_by         VARCHAR(255),
    created_on         TIMESTAMP,
    modified_by        VARCHAR(255),
    modified_on        TIMESTAMP
);

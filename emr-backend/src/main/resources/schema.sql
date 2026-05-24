-- MediConnect IAM schema
-- Tables are created in dependency order so no deferred FK tricks are needed.
-- All CREATE statements use IF NOT EXISTS — safe to re-run on every startup.

CREATE TABLE IF NOT EXISTS rbac_application (
    application_id   BIGSERIAL PRIMARY KEY,
    application_code VARCHAR(255) UNIQUE NOT NULL,
    application_name VARCHAR(255) NOT NULL,
    description      TEXT,
    is_active        BOOLEAN NOT NULL DEFAULT TRUE,
    created_by       INTEGER,
    created_on       TIMESTAMP,
    modified_by      INTEGER,
    modified_on      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rbac_permission (
    permission_id   BIGSERIAL PRIMARY KEY,
    permission_name VARCHAR(255) UNIQUE NOT NULL,
    description     TEXT,
    application_id  BIGINT NOT NULL REFERENCES rbac_application(application_id),
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_by      INTEGER,
    created_on      TIMESTAMP,
    modified_by     INTEGER,
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
    created_by       INTEGER,
    created_on       TIMESTAMP,
    modified_by      INTEGER,
    modified_on      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rbac_user (
    user_id               BIGSERIAL PRIMARY KEY,
    employee_id           BIGINT,
    user_name             VARCHAR(255) UNIQUE NOT NULL,
    password              VARCHAR(255) NOT NULL,
    email                 VARCHAR(255),
    created_by            INTEGER,
    created_on            TIMESTAMP,
    modified_by           INTEGER,
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

-- Usuarios
INSERT INTO users (id, first_name, last_name, password, email, create_date, enabled, token_expired)
VALUES (1, 'John', 'Doe', '$2a$12$NheeI9P250Wfzcb0jrsQbOYDuiTv/4d6BHv5Jq2dQVMG6QKOyERCS', 'john@example.com', CURRENT_TIMESTAMP, true, false);

INSERT INTO users (id, first_name, last_name, password, email, create_date, enabled, token_expired)
VALUES (2, 'Jane', 'Smith', '$2a$12$NheeI9P250Wfzcb0jrsQbOYDuiTv/4d6BHv5Jq2dQVMG6QKOyERCS', 'jane@example.com', CURRENT_TIMESTAMP, true, false);

ALTER SEQUENCE users_seq RESTART WITH 3;

-- Asignar privilegios a los roles
INSERT INTO role_privilege (role_id, privilege_id) VALUES (1, 1); -- Asigna ADMIN_PRIVILEGE al rol admin
INSERT INTO role_privilege (role_id, privilege_id) VALUES (2, 2); -- Asigna USER_PRIVILEGE al rol user

-- Asignar roles a los usuarios
INSERT INTO user_role (user_id, role_id) VALUES (1, 1); -- Asigna el rol admin al usuario con id 1 (John Doe)
INSERT INTO user_role (user_id, role_id) VALUES (2, 2); -- Asigna el rol user al usuario con id 2 (Jane Smith)
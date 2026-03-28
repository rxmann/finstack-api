-- User 1: alice_dev
INSERT INTO users (
    id, provider_type, email, password_hash, roles, username,
    is_active, account_locked, created_at, last_modified_at,
    created_by, last_modified_by, provider_id
)
SELECT
    'b846ef58-a5dd-470b-9d31-4e11ba083c91', 'EMAIL', 'alice@example.com',
    '$2a$10$a1hvPwGX8KpdTyb2IFwNT.LGZfamK1FT.tw8rz6h2knIEEzoHzBIK', '{USER}', 'alice_dev',
    true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    'SYSTEM', 'SYSTEM', 'local-001'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 'b846ef58-a5dd-470b-9d31-4e11ba083c91');

-- User 2: bob_tester
INSERT INTO users (
    id, provider_type, email, password_hash, roles, username,
    is_active, account_locked, created_at, last_modified_at,
    created_by, last_modified_by, provider_id
)
SELECT
    'b846ef58-a5dd-470b-9d31-4e11ba083c92', 'EMAIL', 'bob@example.com',
    '$2a$10$a1hvPwGX8KpdTyb2IFwNT.LGZfamK1FT.tw8rz6h2knIEEzoHzBIK', '{USER}', 'bob_tester',
    true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    'SYSTEM', 'SYSTEM', 'local-002'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 'b846ef58-a5dd-470b-9d31-4e11ba083c92');

-- User 3: charlie_admin
INSERT INTO users (
    id, provider_type, email, password_hash, roles, username,
    is_active, account_locked, created_at, last_modified_at,
    created_by, last_modified_by, provider_id
)
SELECT
    'b846ef58-a5dd-470b-9d31-4e11ba083c93', 'EMAIL', 'charlie@example.com',
    '$2a$10$a1hvPwGX8KpdTyb2IFwNT.LGZfamK1FT.tw8rz6h2knIEEzoHzBIK', '{USER, ADMIN}', 'charlie_admin',
    true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    'SYSTEM', 'SYSTEM', 'local-003'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 'b846ef58-a5dd-470b-9d31-4e11ba083c93');

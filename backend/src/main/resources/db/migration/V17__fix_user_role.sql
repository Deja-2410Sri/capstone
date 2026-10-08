ALTER TABLE users
MODIFY COLUMN role ENUM(
    'role_farmer',
    'role_expert',
    'role_admin'
);
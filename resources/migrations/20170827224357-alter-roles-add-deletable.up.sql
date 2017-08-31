--ALTER TABLE j_roles ADD COLUMN deletable BOOLEAN NOT NULL DEFAULT true;
----;;
ALTER TABLE j_role_rights ADD CONSTRAINT roles_permissions_cnstr UNIQUE (role_id, permission_id);
--;;
ALTER TABLE j_role_members ADD CONSTRAINT roles_users_cnstr UNIQUE (role_id, user_id);

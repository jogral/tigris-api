CREATE OR REPLACE VIEW users_view AS
       SELECT u.*, array_agg(m.role_id ORDER BY m.role_id) AS roles FROM j_users u
       JOIN j_role_members m
       ON u.id = m.user_id
       GROUP BY u.id, u.first_name, u.last_name, u.shortname, u.email, u.password, u.last_login, u.created_on, u.is_active, u.use_sso;

CREATE OR REPLACE VIEW roles_view AS
       SELECT r.*, array_agg(p.permission_id ORDER BY p.permission_id) AS permissions FROM j_roles r
       JOIN j_role_rights p
       ON r.id = p.role_id
       GROUP BY r.id, r.name, r.description, r.is_active, r.deletable;

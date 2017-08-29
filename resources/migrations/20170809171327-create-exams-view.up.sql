CREATE VIEW tests_view AS
       SELECT e.user_id, r.* FROM enrollments e
       JOIN tests_enrollments_rel r
        ON e.id = r.enrollment_id;

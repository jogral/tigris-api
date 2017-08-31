DROP VIEW IF EXISTS quizzes_view;
--;;
ALTER TABLE quizzes_enrollments_rel ALTER COLUMN score TYPE INTEGER;
--;;
CREATE OR REPLACE VIEW quizzes_view AS
       SELECT e.user_id, r.* FROM enrollments e
       JOIN quizzes_enrollments_rel r
            ON e.id = r.enrollment_id;
--;;
DROP VIEW IF EXISTS tests_view;
--;;
ALTER TABLE tests_enrollments_rel ALTER COLUMN score TYPE INTEGER;
--;;
CREATE OR REPLACE VIEW tests_view AS
       SELECT e.user_id, r.* FROM enrollments e
       JOIN tests_enrollments_rel r
        ON e.id = r.enrollment_id;

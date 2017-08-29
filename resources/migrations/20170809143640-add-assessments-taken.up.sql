CREATE TABLE IF NOT EXISTS quizzes_enrollments_rel (
       quiz_id       INTEGER NOT NULL,
       enrollment_id INTEGER NOT NULL,
       score         INTEGER NOT NULL,
       submission    JSONB NOT NULL,
       date_taken    TIMESTAMP WITH TIME ZONE NOT NULL,
       FOREIGN KEY (quiz_id) REFERENCES quizzes (id),
       FOREIGN KEY (enrollment_id) REFERENCES enrollments (id)
);
--;;
CREATE TABLE IF NOT EXISTS tests_enrollments_rel (
       test_id       INTEGER NOT NULL,
       enrollment_id INTEGER NOT NULL,
       score         INTEGER NOT NULL,
       submission    JSONB NOT NULL,
       date_taken    TIMESTAMP WITH TIME ZONE NOT NULL,
       FOREIGN KEY (test_id) REFERENCES tests (id),
       FOREIGN KEY (enrollment_id) REFERENCES enrollments (id)
);
--;;
CREATE VIEW quizzes_view AS
       SELECT e.user_id, r.* FROM enrollments e
       JOIN quizzes_enrollments_rel r
            ON e.id = r.enrollment_id;

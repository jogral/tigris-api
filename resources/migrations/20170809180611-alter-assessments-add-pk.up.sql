CREATE UNIQUE INDEX quizzes_enrollments_rel_idx ON quizzes_enrollments_rel(enrollment_id, quiz_id);
--;;
CREATE UNIQUE INDEX tests_enrollments_rel_idx ON tests_enrollments_rel(enrollment_id, test_id);

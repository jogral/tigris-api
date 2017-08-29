ALTER TABLE quizzes ADD CONSTRAINT quizzes_course_cnstr UNIQUE (course_id, module_id);
--;;
ALTER TABLE tests ADD CONSTRAINT tests_course_cnstr UNIQUE (course_id);

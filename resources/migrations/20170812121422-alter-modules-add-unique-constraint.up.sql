ALTER TABLE modules ADD CONSTRAINT modules_slug_cnstr UNIQUE (course_id, slug);

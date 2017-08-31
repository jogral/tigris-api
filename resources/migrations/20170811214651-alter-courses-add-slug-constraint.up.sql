ALTER TABLE courses ADD CONSTRAINT courses_slug_cnstr UNIQUE (slug, status);

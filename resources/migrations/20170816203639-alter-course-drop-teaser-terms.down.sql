ALTER TABLE COURSES ALTER COLUMN teaser SET NOT NULL;
--;;
ALTER TABLE COURSES ADD COLUMN terms_used CHARACTER VARYING(200)[];
--;;
ALTER TABLE courses ADD COLUMN properties JSONB;
--;;
ALTER TABLE modules ADD COLUMN properties JSONB;
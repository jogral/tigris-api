ALTER TABLE enrollments DROP COLUMN user_id CASCADE;
--;;
ALTER TABLE enrollments ADD COLUMN user_id UUID NOT NULL;
--;;
ALTER TABLE enrollments ADD CONSTRAINT enrollments_user_fk FOREIGN KEY (user_id) REFERENCES j_users(id);


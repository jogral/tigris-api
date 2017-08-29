ALTER TABLE courses ADD COLUMN creator UUID DEFAULT NULL;
--;;
UPDATE courses SET creator = 'eea5ef48-56c1-11e7-9a25-0800270556cb'::uuid;
--;;
ALTER TABLE courses ALTER COLUMN creator SET NOT NULL;
--;;
ALTER TABLE courses ADD CONSTRAINT courses_user_fk FOREIGN KEY (creator) REFERENCES j_users(id);
--;;
ALTER TABLE modules ADD COLUMN description TEXT;
--;;
ALTER TABLE modules ADD COLUMN creator UUID DEFAULT NULL;
--;;
UPDATE modules SET creator = 'eea5ef48-56c1-11e7-9a25-0800270556cb'::uuid;
--;;
ALTER TABLE modules ALTER COLUMN creator SET NOT NULL;
--;;
ALTER TABLE modules ADD CONSTRAINT modules_user_fk FOREIGN KEY (creator) REFERENCES j_users(id);


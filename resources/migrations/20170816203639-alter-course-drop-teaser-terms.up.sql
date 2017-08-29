ALTER TABLE courses ALTER COLUMN teaser DROP NOT NULL;
--;;
ALTER TABLE courses ALTER COLUMN teaser SET DEFAULT NULL;
--;;
ALTER TABLE courses DROP COLUMN terms_used;
--;;
ALTER TABLE courses DROP COLUMN properties;
--;;
ALTER TABLE modules DROP COLUMN properties;


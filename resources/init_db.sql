-- allow low-level commands on a remote DB
CREATE EXTENSION IF NOT EXISTS dblink;

-- ensure required user has been created
DO
$body$
BEGIN
   IF NOT EXISTS (
      SELECT *
      FROM   pg_catalog.pg_user
      WHERE  usename = 'tigris_user') THEN

      CREATE ROLE tigris_user LOGIN PASSWORD 'password1';
   END IF;
END
$body$;

-- ensure required databases have been created
DO
$doDev$
BEGIN

IF EXISTS (SELECT 1 FROM pg_database WHERE datname = 'tigris') THEN
   RAISE NOTICE 'Database tigris already exists';
ELSE
   PERFORM dblink_exec('dbname=' || current_database()  -- current db
                     , 'CREATE DATABASE tigris OWNER tigris_user');
END IF;

END
$doDev$;


DO
$doTest$
BEGIN

IF EXISTS (SELECT 1 FROM pg_database WHERE datname = 'tigris_test') THEN
   RAISE NOTICE 'Database tigris_test already exists';
ELSE
   PERFORM dblink_exec('dbname=' || current_database()  -- current db
                     , 'CREATE DATABASE tigris_test OWNER tigris_user');
END IF;

END
$doTest$;

GRANT ALL PRIVILEGES ON DATABASE tigris to tigris_user;
GRANT ALL PRIVILEGES ON DATABASE tigris_test to tigris_user;

-- add case-insensitive option to both databases
\c tigris;
CREATE EXTENSION IF NOT EXISTS citext;
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

\c tigris_test;
CREATE EXTENSION IF NOT EXISTS citext;
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

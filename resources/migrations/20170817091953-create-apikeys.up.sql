CREATE TABLE IF NOT EXISTS apikeys (
       id           SERIAL PRIMARY KEY,
       name         CHARACTER VARYING(512) NOT NULL,
       token        CHARACTER VARYING(1024) NOT NULL,
       created_on   TIMESTAMP WITH TIME ZONE NOT NULL
);

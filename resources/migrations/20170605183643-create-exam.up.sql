CREATE TABLE IF NOT EXISTS resource_locales (
       id           SERIAL PRIMARY KEY,
       locale       CHARACTER VARYING (32) NOT NULL,
       description  CHARACTER VARYING (128)
);
--;;
CREATE TABLE IF NOT EXISTS quizzes (
       id              SERIAL PRIMARY KEY,
       course_id       INTEGER NOT NULL,
       module_id       INTEGER NOT NULL,
       status          SMALLINT NOT NULL DEFAULT 1,
       created_on      TIMESTAMP WITH TIME ZONE NOT NULL,
       last_updated_on TIMESTAMP WITH TIME ZONE NOT NULL,
       FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
       FOREIGN KEY (module_id) REFERENCES modules (id) ON DELETE CASCADE
);
--;;
CREATE TABLE IF NOT EXISTS tests (
       id              SERIAL PRIMARY KEY,
       course_id       INTEGER NOT NULL,
       status          SMALLINT NOT NULL DEFAULT 1,
       created_on      TIMESTAMP WITH TIME ZONE NOT NULL,
       last_updated_on TIMESTAMP WITH TIME ZONE NOT NULL,
       FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
);
--;;
CREATE TABLE IF NOT EXISTS quiz_questions (
       id            SERIAL PRIMARY KEY,
       quiz_id       INTEGER NOT NULL,
       q_type        CHARACTER VARYING (8) NOT NULL DEFAULT 'fitb',
       status        SMALLINT NOT NULL DEFAULT 1,
       FOREIGN KEY (quiz_id) REFERENCES quizzes (id) ON DELETE CASCADE
);
--;;
CREATE TABLE IF NOT EXISTS quiz_questions_tr (
       question_id    INTEGER NOT NULL,
       lang_id        INTEGER NOT NULL,
       resource_value TEXT,
       FOREIGN KEY (lang_id) REFERENCES resource_locales (id) ON DELETE CASCADE
);
--;;
CREATE UNIQUE INDEX quiz_questions_tr_idx ON quiz_questions_tr (question_id, lang_id);
--;;
CREATE TABLE IF NOT EXISTS test_questions (
       id            SERIAL PRIMARY KEY,
       test_id       INTEGER NOT NULL,
       q_type        CHARACTER VARYING (8) NOT NULL DEFAULT 'fitb',
       status        SMALLINT NOT NULL DEFAULT 1,
       FOREIGN KEY (test_id) REFERENCES tests (id) ON DELETE CASCADE
);
--;;
CREATE TABLE IF NOT EXISTS test_questions_tr (
       question_id    INTEGER NOT NULL,
       lang_id        INTEGER NOT NULL,
       resource_value TEXT,
       FOREIGN KEY (lang_id) REFERENCES resource_locales (id) ON DELETE CASCADE
);
--;;
CREATE UNIQUE INDEX test_questions_tr_idx ON test_questions_tr (question_id, lang_id);
--;;
CREATE TABLE IF NOT EXISTS quiz_answers (
       id           SERIAL PRIMARY KEY,
       question_id  INTEGER,
       is_active    BOOLEAN NOT NULL DEFAULT TRUE,
       FOREIGN KEY (question_id) REFERENCES quiz_questions (id) ON DELETE CASCADE
);
--;;
CREATE TABLE IF NOT EXISTS quiz_answers_tr (
       answer_id      INTEGER NOT NULL,
       lang_id        INTEGER NOT NULL,
       resource_value TEXT,
       FOREIGN KEY (lang_id) REFERENCES resource_locales (id) ON DELETE CASCADE
);
--;;
CREATE UNIQUE INDEX quiz_answers_tr_idx ON quiz_answers_tr (answer_id, lang_id);
--;;
CREATE TABLE IF NOT EXISTS test_answers (
       id           SERIAL PRIMARY KEY,
       question_id  INTEGER,
       is_active    BOOLEAN NOT NULL DEFAULT TRUE,
       FOREIGN KEY (question_id) REFERENCES test_questions (id) ON DELETE CASCADE
);
--;;
CREATE TABLE IF NOT EXISTS test_answers_tr (
       answer_id      INTEGER NOT NULL,
       lang_id        INTEGER NOT NULL,
       resource_value TEXT,
       FOREIGN KEY (lang_id) REFERENCES resource_locales (id) ON DELETE CASCADE
);
--;;
CREATE UNIQUE INDEX test_answers_tr_idx ON test_answers_tr (answer_id, lang_id);
--COPY resource_locales (locale, description) FROM '/home/makiten/projects/alexandria/tigris/api/resources/locales.txt' DELIMITER '|';

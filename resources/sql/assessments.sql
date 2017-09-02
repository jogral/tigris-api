-- :name insert-quiz! :! :n
-- :doc Adds a quiz
INSERT INTO quizzes (course_id,
                     module_id,
                     data,
                     status,
                     created_on,
                     last_updated_on)
             VALUES (:course_id,
                     :module_id,
                     :data,
                     :status,
                     now(),
                     now())

-- :name insert-quiz-taken! :! :n
-- :doc Adds a quiz that a user is taking
INSERT INTO quizzes_enrollments_rel (enrollment_id,
                                     quiz_id,
                                     score,
                                     submission
                                     date_taken)
                             VALUES (:enrollment_id,
                                     :quiz_id,
                                     0,
                                     '{}',
                                     now())

-- :name update-quiz! :! :n
-- :doc Updates quiz
/* :require [clojure.string :as string]
            [hugsql.parameters :refer [identifier-param-quote]] */
UPDATE quizzes SET
/*~
(string/join ","
  (for [[field _] (:updates params)]
    (str (identifier-param-quote (name field) options)
      " = :v:updates." (name field))))
~*/
WHERE id = :id

-- :name update-quiz-taken! :! :n
-- :doc Updates quiz info (only score, submission, and completion)
/* :require [clojure.string :as string]
            [hugsql.parameters :refer [identifier-param-quote]] */
UPDATE quizzes_enrollments_rel SET
/*~
(string/join ","
  (for [[field _] (:updates params)]
    (str (identifier-param-quote (name field) options)
      (if (= (name field) "date_completed")
        " = now()"
        (str " = :v:updates." (name field))))))
~*/
WHERE enrollment_id = :enrollment_id
AND quiz_id = :quiz_id

-- :name get-quiz :? :1
-- :doc Gets a quiz by id
SELECT :i*:cols FROM quizzes
WHERE id = :id

-- :name get-quiz-by-module :? :1
-- :doc Search for quiz by module id
SELECT :i*:cols FROM quizzes
WHERE module_id = :module_id

-- :name get-quizzes-taken :? :*
-- :doc Get all of the quizzes taken
SELECT q.* FROM quizzes_view q

-- :name get-quizzes-taken-by-user :? :*
-- :doc Get quizzes taken by a given user id
SELECT q.* FROM quizzes_view q
WHERE q.user_id = :id

-- :name get-quizzes-taken-by-enrollment :? :*
-- :doc Get quizzes taken by a given enrollment id
SELECT q.* FROM quizzes_view q
WHERE q.enrollment_id = :id

-- :name get-quiz-taken :? :1
-- :doc Gets a quiz taken for a specific quiz and enrollment
SELECT :i*:cols FROM quizzes_view q
WHERE enrollment_id = :enrollment_id
AND quiz_id = :quiz_id

-- :name delete-quiz! :! :n
-- :doc Deletes a quiz
DELETE FROM quizzes
WHERE id = :id

-- :name delete-quiz-taken! :! :n
-- :doc Deletes a quiz taken
DELETE FROM quizzes_enrollments_rel
WHERE enrollment_id = :enrollment_id
AND quiz_id = :quiz_id

-- :name insert-test! :! :n
-- :doc Adds a test
INSERT INTO tests (course_id,
                   data,
                   status,
                   created_on,
                   last_updated_on)
           VALUES (:course_id,
                   :data,
                   :status,
                   now(),
                   now())

-- :name insert-test-taken! :! :n
-- :doc Adds a test that a user is taking
INSERT INTO tests_enrollments_rel (enrollment_id,
                                   test_id,
                                   score,
                                   submission,
                                   date_taken)
                           VALUES (:enrollment_id,
                                   :test_id,
                                   0,
                                   '{}',
                                   now())

-- :name update-test! :! :n
-- :doc Updates test
/* :require [clojure.string :as string]
            [hugsql.parameters :refer [identifier-param-quote]] */
UPDATE tests SET
/*~
(string/join ","
  (for [[field _] (:updates params)]
    (str (identifier-param-quote (name field) options)
      " = :v:updates." (name field))))
~*/
WHERE id = :id

-- :name update-test-taken! :! :n
-- :doc Updates test info (only score, submission, and completion)
/* :require [clojure.string :as string]
            [hugsql.parameters :refer [identifier-param-quote]] */
UPDATE tests_enrollments_rel SET
/*~
(string/join ","
  (for [[field _] (:updates params)]
    (str (identifier-param-quote (name field) options)
      (if (= (name field) "date_completed")
        " = now()"
        (str " = :v:updates." (name field))))))
~*/
WHERE enrollment_id = :enrollment_id
AND test_id = :test_id

-- :name get-test :? :1
-- :doc Gets a test by id
SELECT :i*:cols FROM tests
WHERE id = :id

-- :name get-test-by-course :? :1
-- :doc Search for test by course id
SELECT :i*:cols FROM tests
WHERE course_id = :course_id

-- :name get-tests-taken :? :*
-- :doc Get all of the tests taken
SELECT t.* FROM tests_view t

-- :name get-tests-taken-by-user :? :*
-- :doc Get tests taken by a given user id
SELECT t.* FROM tests_view t
WHERE t.user_id = :id

-- :name get-tests-taken-by-enrollment :? :*
-- :doc Get tests taken by a given enrollment id
SELECT t.* FROM tests_view t
WHERE t.enrollment_id = :id

-- :name get-test-taken :? :1
-- :doc Gets a test taken for a specific quiz and enrollment
SELECT :i*:cols FROM tests_view t
WHERE enrollment_id = :enrollment_id
AND test_id = :test_id

-- :name delete-test! :! :n
-- :doc Deletes a test
DELETE FROM tests
WHERE id = :id

-- :name delete-test-taken! :! :n
-- :doc Deletes a test taken
DELETE FROM tests_enrollments_rel
WHERE enrollment_id = :enrollment_id
AND test_id = :test_id

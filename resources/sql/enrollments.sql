-- :name get-enrollments :? :*
-- :doc select all with cols defined by {:cols [<col_name>...]}
SELECT :i*:cols FROM enrollments

-- :name get-enrollment-by-id :? :1
-- :doc Finds a enrollment by a specific ID
SELECT :i*:cols FROM enrollments e
WHERE e.id = :id
AND e.user_id = :user_id

-- :name get-open-enrollments :? :*
-- :doc Gets currently-enrolled courses
SELECT c.:i*:cols FROM enrollments e
LEFT JOIN courses c
ON e.course_id = c.id
WHERE e.user_id = :user_id
AND e.is_enrolled = true
AND c.status = 1

-- :name get-open-enrollments-for-course :? :*
-- :doc Gets currently-enrolled courses
SELECT :i*:cols FROM enrollments e
WHERE e.user_id = :user_id
AND e.course_id = :course_id
AND e.is_enrolled = true

-- :name insert-enrollment! :! :n
-- :doc Adds a enrollment
INSERT INTO enrollments (user_id,
                         course_id,
                         progress,
                         registered_on)
       VALUES (:user_id::uuid,
               :course_id,
               :progress,
               now())
RETURNING id

-- :name update-enrollment! :! :n
-- :doc Updates a enrollment
/* :require [clojure.string :as string]
            [hugsql.parameters :refer [identifier-param-quote]] */
UPDATE enrollments SET
/*~
(string/join ","
  (for [[field _] (:updates params)]
    (str (identifier-param-quote (name field) options)
      " = :v:updates." (name field))))
~*/
WHERE id = :id
AND user_id = :user_id

-- :name complete! :! :n
-- :doc Completes a course
UPDATE enrollments SET
    completed_on = now()
WHERE id = :id
AND user_id = :user_id

-- :name enroll! :! :n
-- :doc Sets a enrollment's status to true
UPDATE enrollments SET
    is_enrolled = true
WHERE id = :id
AND user_id = :user_id

-- :name unenroll! :! :n
-- :doc Sets a enrollment's status to false
UPDATE enrollments SET
    is_enrolled = false
WHERE id = :id
AND user_id = :user_id

-- :name delete-enrollment! :! :n
-- :doc Deletes enrollment from DB.
DELETE FROM enrollments
WHERE id = :id
AND user_id = :user_id


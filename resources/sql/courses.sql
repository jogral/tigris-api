-- :name active-course-by-id :? :1
-- :doc Finds a course by a specific ID
SELECT :i*:cols FROM courses c WHERE c.id = :id AND c.status = 1

-- :name active-courses :? :*
-- :doc select all active courses
SELECT :i*:cols FROM courses c
WHERE c.status = 1

-- :name all-courses :? :*
-- :doc select all the courses with all the attributes
SELECT * FROM courses

-- :name courses :? :*
-- :doc select all with cols defined by {:cols [<col_name>...]}
SELECT :i*:cols FROM courses

-- :name course-by-id :? :1
-- :doc Finds a course by a specific ID
SELECT :i*:cols FROM courses c WHERE c.id = :id

-- :name course-by-slug :? :1
-- :doc Gets a course by a slug with option
SELECT :i*:cols FROM courses c WHERE c.slug = :slug AND c.status = 1

-- :name course-by-slug-like :? :*
-- :doc Gets a course by a slug with option {:slug_like "P%"}
SELECT :i*:cols FROM courses c WHERE c.slug LIKE :slug-like AND c.status = 1

-- :name recommended-courses :? :*
-- :doc Gets recommended courses
SELECT :i*:cols FROM courses c
WHERE c.status = 1
ORDER BY random()
LIMIT 4

-- :name insert-course! :<!
-- :doc Adds a course
INSERT INTO courses (title,
                    slug,
                    teaser,
                    description,
                    long_description,
                    tags,
                    image,
                    status,
                    creator,
                    created_on,
                    last_updated_on)
       VALUES (:title,
               :slug,
               :teaser,
               :description,
               :long_description,
               :tags,
               :image,
               :status,
               :creator::uuid,
               now(),
               now())
RETURNING id

-- :name update-course! :! :n
-- :doc Updates a course
/* :require [clojure.string :as string]
            [hugsql.parameters :refer [identifier-param-quote]] */
UPDATE courses SET
/*~
(string/join ","
  (for [[field _] (:updates params)]
    (str (identifier-param-quote (name field) options)
      (if (= (name field) "creator")
        (str " = :v:updates." (name field) "::uuid")
        (str " = :v:updates." (name field))))))
~*/
WHERE id = :id

-- :name activate-course! :! :n
-- :doc Sets a course's status to 1
UPDATE courses SET
    status = 1
WHERE id = :id

-- :name make-tentative-course! :! :n
-- :doc Sets a course's status to 0
UPDATE courses SET
    status = 0
WHERE id = :id

-- :name deactivate-course! :! :n
-- :doc Sets a course's status to -1
UPDATE courses SET
    status = -1
WHERE id = :id

-- :name delete-course! :! :n
-- :doc Deletes a course
DELETE FROM courses
WHERE id = :id

-- :name delete-course-modules! :! :n
-- :doc Deletes all modules from course :id
DELETE FROM modules mo
WHERE mo.course_id = :id

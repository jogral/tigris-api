-- :name get-active-modules :? :*
-- :doc Get all active modules
SELECT :i*:cols FROM modules mo
WHERE mo.is_active = true

-- :name get-all-modules :? :*
-- :doc select all the modules with all the attributes
SELECT * FROM modules

-- :name get-modules-by-course :? :*
-- :doc Select modules by course id
SELECT :i*:cols FROM modules mo
WHERE mo.course_id = :course-id
ORDER BY mo.order_index ASC

-- :name get-module-in-course :? :1
-- :doc Select module in course by id
SELECT * FROM modules mo
WHERE mo.course_id = :course-id
AND mo.id = :module-id

-- :name get-modules :? :*
-- :doc select all with cols defined by {:cols [<col_name>...]}
SELECT :i*:cols FROM modules

-- :name get-module-by-id :? :*
-- :doc Finds a module by a specific ID
SELECT :i*:cols FROM modules mo WHERE mo.id = :id

-- :name get-module-by-slug-like :? :*
-- :doc Gets a module by a slug with option {:slug-like "P%"}
SELECT :i*:cols FROM modules mo WHERE mo.slug LIKE :slug-like

-- :name get-module-by-slug :? :1
-- :doc Gets a module by a slug with option {:slug-like "P%"}
SELECT :i*:cols FROM modules mo WHERE mo.slug = :slug

-- :name get-next-module-slug :? :1
-- :doc Retrieves the next slug
SELECT slug FROM modules mo
WHERE order_index = (SELECT order_index + 1 FROM modules
                     WHERE id = :id AND course_id = :course_id)

-- :name insert-module! :<!
-- :doc Adds a module
INSERT INTO modules (course_id,
                    order_index,
                    title,
                    slug,
                    description,
                    content,
                    creator,
                    created_on,
                    last_updated_on)
       VALUES (:course-id,
               :order_index,
               :title,
               :slug,
               :description,
               :content,
               :creator::uuid,
               now(),
               now())
RETURNING id

-- :name update-module! :! :n
-- :doc Updates a module
/* :require [clojure.string :as string]
            [hugsql.parameters :refer [identifier-param-quote]] */
UPDATE modules SET
/*~
(string/join ","
  (for [[field _] (:updates params)]
    (str (identifier-param-quote (name field) options)
      (if (= (name field) "creator")
        (str " = :v:updates." (name field) "::uuid")
        (str " = :v:updates." (name field))))))
~*/
WHERE id = :id
AND course_id = :course_id

-- :name activate-module! :! :n
-- :doc Sets a module's status to 1
UPDATE modules SET
    is_active = true
WHERE id = :id
AND course_id = :course_id

-- :name deactivate-module! :! :n
-- :doc Sets a module's status to -1
UPDATE modules SET
    is_active = false
WHERE id = :id
AND course_id = :course_id

-- :name delete-module! :! :n
-- :doc Deletes a module from database
DELETE FROM modules
WHERE id = :id
AND course_id = :course_id

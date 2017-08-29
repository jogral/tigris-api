-- :name activate-permission! :! :n
-- :doc Activates a permission in the DB.
update j_permissions set
is_active = true
where id = :id

-- :name create-permission! :! :n
-- :doc Creates a permission
/* :require [clojure.string :as string] */
insert into j_permissions
(
/*~
(string/join "," (map name (keys (:fields params))))
~*/
)
VALUES (
/*~
(string/join "," (keys (:fields params)))
~*/
)
RETURNING id

-- :name deactivate-permission! :! :n
-- :doc Deactivates a permission in the DB.
update j_permissions set
is_active = false
where id = :id

-- :name delete-permission! :! :n
-- :doc Deletes a permission
delete from j_permissions
where id = :id

-- :name get-permission :? :1
-- :doc Gets a permission by ID from the DB.
select * from j_permissions
where id = :id

-- :name get-permissions :? :*
-- :doc Gets all permissions from the DB.
select * from j_permissions


-- :name update-permission! :! :n
-- :doc Updates permission details in the DB.
/* :require [clojure.string :as string]
            [hugsql.parameters :refer [identifier-param-quote]] */
update j_permissions set
/*~
(string/join ","
  (for [[field _] (:updates params)]
    (str (identifier-param-quote (name field) options)
      " = :v:updates." (name field))))
~*/
where id = :id

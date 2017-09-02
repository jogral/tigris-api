-- :name activate-role! :! :n
-- :doc Activates a role
update j_roles set
is_active = true
where id = :id

-- :name create-role! :<!
-- :doc Creates a role
/* :require [clojure.string :as string] */
insert into j_roles (name, description)
VALUES (:name, :description)
RETURNING id

-- :name create-role-rel! :! :n
-- :doc Creates a relation with a role
INSERT INTO j_role_rights (role_id, permission_id)
VALUES (:role_id, :permission_id)

-- :name deactivate-role! :! :n
-- :doc Deactivates a role
update j_roles set
is_active = false
where id = :id

-- :name delete-role! :! :n
-- :doc Deletes a role
delete from j_roles
where id = :id

-- :name delete-role-rel! :! :n
-- :doc Deletes a relation with a role
DELETE FROM j_role_rights
WHERE role_id = :role_id
AND permission_id = :permission_id

-- :name get-roles :? :*
-- :doc gets roles
select :i*:cols from roles_view

-- :name get-role :? :1
-- :doc retrieve a role by id
select :i*:cols from roles_view v
where v.id = :id

-- :name get-user-roles :? :*
-- :doc get user roles
select * from j_roles
where id in 
(select role_id
 from j_role_members
 where user_id = :id)

-- :name get-role-permissions :? :*
-- :doc gets all permissions in a role
select * from j_permissions
where id in
(select permission_id
 from j_role_rights
 where role_id = :id)

-- :name update-role! :! :n
-- :doc Updates a role
/* :require [clojure.string :as string]
            [hugsql.parameters :refer [identifier-param-quote]] */
update j_roles set
/*~
(string/join ","
  (for [[field _] (:updates params)]
    (str (identifier-param-quote (name field) options)
      " = :v:updates." (name field))))
~*/
WHERE id = :id

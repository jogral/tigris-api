-- :name add-to-role! :! :*
-- :doc Adds user to role (group)
INSERT INTO j_role_members (user_id, role_id)
VALUES (:user_id::uuid, :role_id)

-- :name create-user! :<!
-- :doc creates a new user record
INSERT INTO j_users
(shortname, email, created_on, last_login)
VALUES (lower(:email), lower(:email), now(), now())
RETURNING id

-- :name update-user! :! :n
-- :doc update an existing user record
/* :require [clojure.string :as string]
            [hugsql.parameters :refer [identifier-param-quote]] */
UPDATE j_users SET
/*~
(string/join ","
  (for [[field _] (:updates params)]
    (str (identifier-param-quote (name field) options)
      (if (= (name field) "password")
        (str " = crypt(:v:updates." (name field) ", gen_salt('bf', 8))")
        (str " = :v:updates." (name field))))))
~*/
WHERE id = :id

-- :name get-active-users :? :*
-- :doc retrieve active users.
SELECT :i*:cols FROM users_view
WHERE is_active = true

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT :i*:cols FROM users_view
WHERE id = :id

-- :name get-users :? :*
-- :doc retrieve active users.
SELECT :i*:cols FROM users_view

-- :name get-user-by-col :? :1
-- :doc Gets a user by a column. Returns only one, since likely using unique columns.
SELECT :i*:cols FROM users_view
WHERE :search_col = :val

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM j_users
WHERE id = :id

-- :name authenticate-login-by-email :? :1
-- :doc Authenticate username and password
select :i*:cols from users_view
where email = lower(:email)
and password = crypt(:password, password)

-- :name authenticate-login-by-alias :? :1
-- :doc Authenticate username and password
select :i*:cols from users_view
where shortname = lower(:alias)
and password = crypt(:password, password)

-- :name delete-from-role! :! :*
-- :doc Removes user from role (group)
DELETE FROM j_role_members
WHERE user_id =:user_id::uuid

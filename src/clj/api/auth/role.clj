(ns api.auth.role
  "Normal Tigris role API"
  (:require
   [api.db.core :as db]
   [clojure.string :as str]))

(def role-cols
  "Columns for the row"
  ["id"
   "name"
   "description"
   "is_active"
   "deletable"
   "permissions"])

(defn activate
  "Activates a role."
  [id]
  (db/activate-role! {:id id}))

(defn create-role-permission
  "Assigns a permission to a role."
  [role-id perm-id]
  (db/create-role-rel! {:role_id role-id :permission_id perm-id}))

(defn create-role-permissions
  "Assigns permissions to a role."
  [role-id perm-ids]
  (doseq [p perm-ids] (create-role-permission role-id p)))

(defn create
  "Makes a new role."
  [fields]
  (db/create-role! fields))

(defn deactivate
  "Deactivates a role."
  [id]
  (db/deactivate-role! {:id id}))

(defn delete
  "Deletes a role."
  [id]
  (db/delete-role! {:id id}))

(defn delete-role-permission
  "Deletes a permission from a role."
  [role-id perm-id]
  (db/delete-role-rel! {:role_id role-id :permission_id perm-id}))

(defn delete-role-permissions
  "Deletes permissions from a role."
  [role-id perm-ids]
  (doseq [p perm-ids] (delete-role-permission role-id p)))

(defn find-one
  "Gets a role by role ID"
  [id]
  (db/get-role {:id id :cols role-cols}))

(defn find-all
  "Gets all roles."
  []
  (db/get-roles {:cols role-cols}))

(defn find-permissions
  "Gets all permissions in the role."
  [id]
  (db/get-role-permissions {:id id}))

(defn modify
  "Updates a role."
  [id fields]
  (db/update-role! {:id id :updates fields}))

(ns api.auth.permission
  "Permissions API"
  (:require
   [api.db.core :as db]
   [api.util.core :refer [decrypt-token]]
   [clojure.string :as str]))

;; Heavily inspired by DRF

(defn activate
  "Activates a permission."
  [id]
  (db/activate-permission! {:id id}))

(defn create
  "Makes a new permission."
  [fields]
  (db/create-permission! {:fields fields}))

(defn deactivate
  "Deactivates a permission."
  [id]
  (db/deactivate-permission! {:id id}))

(defn delete
  "Deletes a permission."
  [id]
  (db/delete-permission! {:id id}))

(defn retrieve-permission
  "Gets a permission by permission ID"
  [id]
  (db/get-permission {:id id}))

(defn retrieve-permissions
  "Gets all permissions."
  []
  (db/get-permissions))

(defn modify
  "Updates a permission."
  [id fields]
  (db/update-permission! {:id id :fields fields}))

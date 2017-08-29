(ns api.auth.user
  "Normal Tigris user API"
  (:require
   [api.auth.core :refer [valid-email?]]
   [api.util.core :refer [any?]]
   [api.config :refer [env]]
   [api.db.core :as db]
   [api.platform.enrollments :refer [retrieve-enrollments-by-user]]
   [clojure.string :as str]
   [clojure.tools.logging :as log]))

(def user-cols
  "Columns to search table"
  ["id"
   "email"
   "first_name"
   "last_name"
   "shortname"
   "roles"
   "created_on"
   "last_login"
   "is_active"
   "use_sso"])

(defn sanitize
  "Clean up fields for submission."
  [fields]
  (try
    (let [details (dissoc fields :created_on :last_login)
          details (if (contains? details :password)
                    (assoc
                     (into
                      details
                      (map (fn [[k v]] [k (if (string? v) (str/lower-case v) v)]) details))
                     :password
                     (:password fields))
                    details)]
      details)
    (catch Exception e
      fields)))

(defn activate
  "Activate a user."
  [id]
  (db/update-user! {:id id :updates {:is_active true}}))

(defn create
  "Creates a new user."
  [fields]
  (if (and (contains? fields :password) (< (.length (:password fields)) 8))
    (throw (Throwable. "Password must be 8 or more characters."))
    (if (vector? fields)
      (map #(db/create-user! %) fields)
      (db/create-user! fields))))

(defn deactivate
  "Deactivate user."
  [id]
  (db/update-user! {:id id :updates {:is_active false}}))

(defn delete
  "Deletes a user."
  [id]
  (db/delete-user! {:id id}))

(defn find-one
  "Gets a user by ID."
  [id]
  (let [user (db/get-user {:id id :cols user-cols})
        user (assoc user :enrollments (retrieve-enrollments-by-user id))]
    user))

(defn find-one-by-col
  "Gets a user by unique column (email or shortname)."
  [col val]
  (let [col  (if (or (not= col "shortname") (not= col "email")) "shortname" col)
        user (db/get-user-by-col {:cols user-cols :search_col col :val val})
        user (when (not (nil? user))
               (assoc user :enrollments (retrieve-enrollments-by-user (:id user))))]
    user))

(defn find-active
  "Gets all active users."
  []
  (let [users (db/get-active-users {:cols user-cols})
        users (map (fn [u]
                     (assoc u :enrollments (retrieve-enrollments-by-user (:id u))))
                   users)]
    users))

(defn find-all
  "Gets all users."
  []
  (let [users (db/get-users {:cols user-cols})
        users (map (fn [u]
                     (assoc u :enrollments (retrieve-enrollments-by-user (:id u))))
                   users)]
    users))

(defn modify
  "Updates a user."
  [id updates]
  (let [details (sanitize updates)]
    (db/update-user! {:id id :updates details})))

(defn user-roles
  "Finds roles by user ID."
  [id]
  (db/get-user-roles {:id id}))

(defn authenticate
  "Authenticates user by shortname or email"
  [username password]
  (try
    (if (valid-email? username)
      (db/authenticate-login-by-email {:cols user-cols :email username :password password})
      (db/authenticate-login-by-alias {:cols user-cols :alias username :password password}))
    (catch Throwable t
      (when (:dev env)
        (log/warn t))
      nil)))
      
(defn add-to-role
  "Adds user to group."
  [user-id role-id]
  (let [user    (find-one user-id)
        member? (any? #(= role-id %) (:roles user))
        _       (when-not member?
                  (db/delete-from-role! {:user_id user-id})) ;; TODO: Eventually this will disappear
        result  (if-not member?
                  (db/add-to-role! {:user_id user-id :role_id role-id})
                  0)]
    result))


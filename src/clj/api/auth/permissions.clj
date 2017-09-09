(ns api.auth.permissions
  (:require
   [api.auth.user :refer [find-one]]
   [api.platform.enrollments :refer [retrieve-enrollment]]
   [api.notifications.core :refer [get-one]]
   [api.db.core :as db]
   [api.util.core :as util]
   [buddy.auth :refer [authenticated?]]
   [clojure.string :refer [split]]
   [clojure.tools.logging :as log]))

(def safe-methods #{"GET" "HEAD" "OPTIONS"})


(defn login?
  "Checks if a user is logging in."
  [req]
  (let [uri    (.toLowerCase (:uri req))
        login? (boolean (re-find #"/users/authenticate" uri))]
    login?))


(defn read-only?
  ""
  [req]
  (let [method (.toUpperCase (name (:request-method req)))
        safe?  (and
                (authenticated? req)
                (some #(= method %) safe-methods))]
    safe?))

(defn self?
  "Is the owner changing this?"
  [req]
  (try
    (let [decrypted  (util/decrypt-token (get-in req [:identity :token]))
          user-id    0
          self?      (= (:user decrypted) user-id)]
      self?)
    (catch Throwable t
      false)))


(defn assessment-owner?
  ""
  [req]
  (try
    (let [token         (req :identity)
          enrollment-id (get-in req [:params :enrollment-id])
          enrollment    (try
                          (retrieve-enrollment enrollment-id (:user token))
                          (catch Throwable t
                            nil))
          user-id       (if-not (nil? enrollment)
                          (:user_id enrollment)
                          nil)
          self?         (and
                         (authenticated? req)
                         (= (:user token) user-id))]
      self?)
    (catch Throwable t
      false)))

(defn recipient?
  "Are you a recipient?"
  [req]
  (let [user-id        (get-in req [:identity :user])
        has-recipient? (contains? (req :params) :recipient)
        notification   (get-one (get-in req [:params :id]))
        recipients     (try
                         (into [] (map #(Long/parseLong %) (split (:recipients notification) #",")))
                         (catch Throwable t
                           nil))
        recipient      (if has-recipient?
                         (get-in req [:params :recipient])
                         (some #{user-id} recipients))
        recipient?     (= user-id recipient)]
    recipient?))

(defn sender?
  "Are you the sender?"
  [req]
  (let [user-id     (get-in req [:identity :user])
        has-sender? (contains? (req :params) :sender)
        sender      (if has-sender?
                      (get-in req [:params :sender])
                      (:sender_id (get-one (get-in req [:params :id]))))
        sender?     (= user-id sender)]
    sender?))


(defn admin?
  "Is user an admin?"
  [req]
  (try
    (let [token      (req :identity)
          user-id    (:user token)
          user       (find-one user-id)
          cols       ["id"
                      "name"
                      "description"
                      "is_active"
                      "deletable"
                      "permissions"]
          admin-role (db/get-admin-role {:cols cols})
          admin?     (some #(= (:id admin-role) %) (:roles user))]
      admin?)
    (catch Throwable t
      (log/warn t)
      false)))

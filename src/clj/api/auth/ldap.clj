(ns api.auth.ldap
  (:require
   [api.config :refer [env]]
   [clj-ldap.client :as client]
   [mount.core :refer [defstate]]))

; Taken mostly from http://www.luminusweb.net/docs/security.md

(def host (:ldap-settings env))

(defstate ldap-pool :start (client/connect host))

(defn authenticate [username password domain-group & [attributes]]
  "Authenticates a user against an LDAP directory."
  (let [conn           (client/get-connection ldap-pool)
        qualified-name (str username "@" (-> host :host :address))]
    (try
      (if (client/bind? conn qualified-name password)
        (first (client/search conn
                              domain-group
                              {:filter     (str "sAMAccountName=" username)
                               :attributes (or attributes [])})))
      (finally (client/release-connection ldap-pool conn)))))


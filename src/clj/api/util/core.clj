(ns api.util.core
  "Utility functions for miscellaneous things."
  (:require
   [api.config :refer [env]]
   [api.middleware :refer [privkey pubkey]]
   [buddy.sign.jwt :as jwt]
   [clj-time.coerce :as c]
   [clj-time.core :as t]
   [clj-time.format :as f]
   [clojure.data.codec.base64 :as b64]
   [clojure.string :as str]
   [clojure.tools.logging :as log]))

(def token-date-formatter (f/formatters :basic-date-time))
(def any? (complement not-any?))

(defn valid-token?
  "Check if the token is valid or not"
  [token]
  (try
    (let [token     (str/replace token #"((?i)Bearer )" "")
          decrypted (jwt/decrypt token privkey {:alg :rsa-oaep :enc :a128cbc-hs256})]
          ;;expired?  (t/after? (c/to-local-date-time (t/now))
          ;;                    (c/to-local-date-time (c/to-long (:exp decrypted))))]
      (= (:iss decrypted) "tigris"))
    (catch Throwable t
      ;;(when (:dev env)
        (log/warn t);;)
      false)))

(defn process-invitation-token
  "Gets decrypted token if it's valid."
  [token]
  (try
    (let [unsigned (jwt/unsign token pubkey {:alg :rs256})]
      unsigned)
    (catch Throwable t
      nil)))

(defn validate-invitation-token
  "Confirm an invite token."
  [token]
  (try
    (let [decoded  (String. (b64/decode (.getBytes token)))
          unsigned (process-invitation-token decoded)
          expired? (t/after? (c/to-local-date-time (t/now))
                             (c/to-local-date-time (c/to-long (:exp unsigned))))]
      {:valid? expired? :id (:id unsigned) :email (:email unsigned)})
    (catch Throwable t
      (when (:dev env)
        (log/warn t))
      {:valid? false})))

(defn generate-invitation-token
  "Generate an invite confirmation token."
  ([id email]
   (generate-invitation-token id email 24))
  ([id email time]
   (let [token   {:id    id
                  :email email
                  :iss   "tigris"
                  :iat   (t/now)
                  :exp   (t/plus (t/now) (t/hours time))}
         signed  (jwt/sign token privkey {:alg :rs256})
         encoded (String. (b64/encode (.getBytes signed)) "UTF-8")]
     encoded)))

(defn generate-token
  "Generate a user JWT."
  [user]
  (let [now       (t/now)
        token     {:user (:id user)
                   :iss  "tigris"
                   :iat  now}
                   ;;:exp  (t/plus now (t/hours 8))}
        encrypted (jwt/encrypt token pubkey {:alg :rsa-oaep :enc :a128cbc-hs256})]
    encrypted))

(defn refresh-token
  "Refresh a user JWT."
  [token]
  (let [token     (str/replace token #"((?i)Bearer )" "")
        _         (when-not (valid-token? token)
                    (throw (Throwable. "Provided token is not valid.")))
        decrypted (jwt/decrypt token privkey {:alg :rsa-oaep :enc :a128cbc-hs256})
        new-token (assoc decrypted :iat (t/now))
        encrypted (jwt/encrypt new-token pubkey {:alg :rsa-oaep :enc :a128cbc-hs256})]
    encrypted))


(defn upload-file
  ""
  [file]
  file)

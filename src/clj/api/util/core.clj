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
   [clojure.tools.logging :as log])
  (:import
   [com.microsoft.azure.storage CloudStorageAccount]
   [com.microsoft.azure.storage.blob CloudBlobClient
                                     CloudBlobContainer
                                     CloudBlockBlob]
   [java.io File]))

(def token-date-formatter (f/formatters :basic-date-time))
(def any? (complement not-any?))
(def azure-acct (CloudStorageAccount/parse "DefaultEndpointsProtocol=https;AccountName=jogralmedia;AccountKey=KFkfhmC+b8rbeABdyUp+8kbSYBK1KBtCKQCmqaEomDp3nH5atzYrA2gTrRWsZ2JXnajq3HmqiG9OJgtcSy+Svw==;EndpointSuffix=core.windows.net"))

(defn rand-str [len]
  (apply str (take len (repeatedly #(char (+ (rand 26) 65))))))

(defn decrypt-token
  "Decrypts a token"
  [token]
  (let [token     (str/replace token #"((?i)Bearer )" "")
        token     (str/replace token #"((?i)Token )" "")
        decrypted (jwt/decrypt token privkey {:alg :rsa-oaep :enc :a128cbc-hs256})]
    decrypted))

(defn valid-token?
  "Check if the token is valid or not"
  [token]
  (try
    (let [decrypted (decrypt-token token)
          expired?  (t/after? (c/to-local-date-time (t/now))
                              (c/to-local-date-time (c/to-long (:exp decrypted))))]
      (and expired? (= (:iss decrypted) "tigris")))
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
                   :iat  now
                   :exp  (t/plus now (t/hours 8))}
        encrypted (jwt/encrypt token pubkey {:alg :rsa-oaep :enc :a128cbc-hs256})]
    encrypted))

(defn refresh-token
  "Refresh a user JWT."
  [token]
  (let [token     (str/replace token #"((?i)Bearer )" "")
        token     (str/replace token #"((?i)Token )" "")
        _         (when-not (valid-token? token)
                    (throw (Throwable. "Provided token is not valid.")))
        decrypted (jwt/decrypt token privkey {:alg :rsa-oaep :enc :a128cbc-hs256})
        new-token (assoc decrypted :iat (t/now))
        encrypted (jwt/encrypt new-token pubkey {:alg :rsa-oaep :enc :a128cbc-hs256})]
    encrypted))


(defn upload-file
  ""
  [tempfile filename]
  (let [container-name (if (:production env) "production" "test")
        client         (.createCloudBlobClient azure-acct)
        container      (.getContainerReference client container-name)
        file-pieces    (str/split filename #"\.")
        new-filename   (if (< (count (first file-pieces)) 3) (rand-str 3) (first file-pieces))
        new-file       (File/createTempFile new-filename (str "." (last file-pieces)))
        blob           (.getBlockBlobReference container (.getName new-file))
        _              (.uploadFromFile blob (.getAbsolutePath tempfile))]
    (.toString (.getSnapshotQualifiedUri blob))))

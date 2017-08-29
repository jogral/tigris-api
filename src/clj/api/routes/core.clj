(ns api.routes.core
  "Util functions."
  (:require
   [api.config :refer [env]]
   [api.util.core :refer [valid-token? generate-token refresh-token]]
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [ring.util.http-response :as respond]))

(defn respond-or-catch
  "Responds with a Ring response or give a bad request and log error"
  [func error-msg]
  (try
    (func)
    (catch Throwable t
      (when (:dev env)
        (log/warn t))
      (respond/bad-request {:error error-msg}))))

(defn validate-and-respond
  "Validate that a user JWT is not expired."
  [token resp error-msg]
  (if (valid-token? token)
    (respond-or-catch resp error-msg)
    (respond/forbidden "Could not validate user authorization.")))

(ns user
  (:require [mount.core :as mount]
            api.core))

(defn start []
  (mount/start-without #'api.core/repl-server))

(defn stop []
  (mount/stop-except #'api.core/repl-server))

(defn restart []
  (stop)
  (start))



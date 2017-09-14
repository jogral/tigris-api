(ns api.service.verbs
  (:require [clojure.string :refer [join]]))

(def base-url "http://activitystrea.ms/schema/1.0")
(def default-locale "en-US")
(def concat-url #(join "/" [base-url %]))

(defn create-verb
  [uri description]
  (let [verb {:id (concat-url uri)
              :display {(keyword default-locale) description}}]
    verb))


(def added (create-verb "add" "added"))
(def completed (create-verb "complete" "completed"))
(def failed (create-verb "failed" "failed"))
(def initialized (create-verb "initialize" "initialized"))
(def mastered (create-verb "mastered" "mastered"))
(def passed (create-verb "passed" "passed"))
(def started (create-verb "start" "started"))

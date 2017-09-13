(ns api.service.xapi
  (:require
   [api.service.verbs :as verbs]
   [clj-uuid :as uuid]
   [clojure.string :refer [join]]))


(defn action
  "Common action for xAPI."
  [{:keys [shortname email]}
   verb
   {:keys [uri]}
   timestamp]
  (let [actor     {:name shortname
                   :mbox email
                   :objectType "Agent"}
        activity  {:id uri
                   :objectType "Activity"}
        statement {:id        (uuid/v1)
                   :actor     actor
                   :verb      verb
                   :activity  activity
                   :timestamp timestamp}]
    statement))

(defn create-verb
  ""
  [{:keys [uri description]}]
  (let [verb {:id (join "/" uri description)
              :display {(keyword "en-US") description}}]
    verb))


(defn created-course
  ""
  [user activity]
  (let [verb {:uri         "http://activitystrea.ms/schema/1.0/create"
              :description "created"}]
    (action user (create-verb verb) activity)))

(defn enrolled-in
  ""
  [user activity]
  (action user verbs/initialized activity))

(defn failed
  ""
  [user activity]
  (action user verbs/failed activity))

(defn mastered
  ""
  [user activity]
  (action user verbs/mastered activity))

(defn passed
  ""
  [user activity]
  (action user verbs/passed activity))

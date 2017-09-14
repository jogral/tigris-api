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
  [user activity & timestamp]
  (let [verb {:uri         "http://activitystrea.ms/schema/1.0/create"
              :description "created"}]
    (action user (create-verb verb) activity timestamp)))

(defn completed
  ""
  [user activity & timestamp]
  (action user verbs/completed activity timestamp))

(defn enrolled-in
  ""
  [user activity & timestamp]
  (action user verbs/initialized activity timestamp))

(defn failed
  ""
  [user activity score & timestamp]
  (let [statement (action user verbs/failed activity timestamp)
        statement (assoc statement :result {:score
                                            {:scaled (/ score 100)
                                             :raw    score
                                             :min    0
                                             :max    100} 
                                            :success false
                                            :completed true})]
    statement))

(defn mastered
  ""
  [user activity & timestamp]
  (action user verbs/mastered activity timestamp))

(defn passed
  ""
  [user activity score & timestamp]
  (let [statement (action user verbs/passed activity timestamp)
        statement (assoc statement :result {:score
                                            {:scaled (/ score 100)
                                             :raw    score
                                             :min    0
                                             :max    100} 
                                            :success true
                                            :completed true})]
    statement))

(defn started
  ""
  [user activity & timestamp]
  (action user verbs/started activity timestamp))

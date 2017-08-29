(ns api.notifications.core
  "The Notifications details."
  (:require
   [api.auth.user :as user]
   [api.db.core :refer [*db*] :as db]
   [api.db.core :as query]
   [clojure.java.jdbc :as jdbc]))

(defrecord Notification [id sender-id message created sent available])

(def notification-cols ["id" "title" "sender_id" "message" "is_read" "created" "sent" "available"])

(defn delete
  ""
  [id]
  (let [result (query/delete-notification! {:id id})]
    result))

(defn get-one
  ""
  [id]
  (let [notification (query/get-notification {:id id :cols notification-cols})]
    notification))

(defn get-all
  ""
  []
  (let [notifications (query/get-all-notifications {:cols notification-cols})]
    notifications))

(defn get-by-recipient
  ""
  [recipient]
  (let [notifications (query/get-notifications-by-recipient {:recipient (.toString recipient)
                                                          :cols notification-cols})
        notifications (map (fn [n]
                             (let [sender (user/find-one (:sender_id n))
                                   n      (dissoc n :sender_id)
                                   n      (assoc n :sender sender)]
                               n)) notifications)]
    notifications))
        
(defn get-by-sender
  ""
  [sender]
  (let [notifications (query/get-notifications-by-sender {:sender sender
                                                          :cols notification-cols})]
    notifications))

(defn create
  ""
  ([notification]
   (let [data (dissoc notification :id :sent)]
     (query/add-notification! data)))
  ([notification recipients]
   (let [data            (dissoc notification :id)
         notification-id (first (query/add-notification! data))
         relations       (map #(hash-map :notification (:id notification-id) :recipient %) recipients)
         _               (doseq [r relations] (query/add-recipient! r))]
     notification-id)))

(defn read-message
  ""
  [notification recipient]
  (let [result (query/read-notification! {:notification notification :recipient recipient})]
    result))

(defn unread-message
  ""
  [notification recipient]
  (let [result (query/unread-notification! {:notification notification :recipient recipient})]
    result))

(defn modify
  ""
  [id notification]
  (let [notification (dissoc notification :sender_id :id :sent)
        result       (query/update-notification! {:updates notification :id id})]
    result))

(defn send-message
  ""
  [id]
  (let [result (query/update-notification! {:updates {:sent "now()"} :id id})]
    result))

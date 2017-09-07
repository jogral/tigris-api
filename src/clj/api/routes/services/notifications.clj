(ns api.routes.services.notifications
  "The routes for the Notifications section"
  (:require
   [api.auth.permissions :refer [admin? recipient? sender?]]
   [api.notifications.core :as notifications]
   [api.routes.core :refer [validate-and-respond]]
   [buddy.auth :refer [authenticated?]]
   [clojure.string :refer [blank?]]
   [compojure.api.sweet :refer [context DELETE GET PUT POST]]
   [ring.util.http-response :as respond]
   [schema.core :as s]))

(defn add-notification
  ""
  [notification recipients token]
  (validate-and-respond
   token
   #(let [id (if (empty? recipients)
               (notifications/create notification)
               (notifications/create notification recipients))]
      (respond/ok {:result id}))
   "Cannot add NOTIFICATION."))

(defn delete-notification
  ""
  [id token]
  (validate-and-respond
   token
   #(respond/ok {:result (notifications/delete id)})
   (str "Cannot add NOTIFICATION " (:id id) ".")))

(defn get-all-notifications
  "Gets all notifications."
  [token]
  (validate-and-respond
   token
   #(respond/ok (notifications/get-all))
   "Cannot get NOTIFICATIONS."))

(defn get-notification
  ""
  [id token]
  (validate-and-respond
   token
   #(respond/ok (notifications/get-one id))
   (str "Cannot get NOTIFICATION " id ".")))

(defn get-notifications-by-recipient
  ""
  [recipient token]
  (validate-and-respond
   token
   #(respond/ok (notifications/get-by-recipient recipient))
   "Cannot get NOTIFICATIONS."))

(defn get-notifications-by-sender
  ""
  [sender-id token]
  (validate-and-respond
   token
   #(let [result (notifications/get-by-sender sender-id)]
      (respond/ok result))
   "Cannot get NOTIFICATIONS."))

(defn read-notification
  ""
  [notification recipient token]
  (validate-and-respond
   token
   #(respond/ok {:result (notifications/read-message notification recipient)})
   (str "Cannot update NOTIFICATION " notification ".")))

(defn send-notification
  ""
  [id]
  #(let [result (notifications/send-message id)]
     (respond/ok {:sent result})))

(defn unread-notification
  ""
  [notification recipient token]
  (validate-and-respond
   token
   #(respond/ok {:result (notifications/unread-message notification recipient)})
   (str "Cannot update NOTIFICATION " notification ".")))

(defn update-notification
  ""
  [id notification token]
  (validate-and-respond
   token
   #(let [result (notifications/modify id notification)]
      (respond/ok {:result result}))
   (str "Cannot update NOTIFICATION " id ".")))


(def notification-context
  "The routes for notifications."
  (context
   "/api/notifications"
   []
   :tags ["notification"]
   (DELETE "/:id"         {:as request}
           :auth-rules    {:or [admin? sender?]}
           :summary       ""
           :description   ""
           :header-params [authorization :- String]
           :path-params   [id :- Long]
           (delete-notification id authorization))
   (GET "/"            {:as request}
        :auth-rules    {:or [admin? recipient? sender?]}
        :summary       ""
        :description   ""
        :header-params [authorization :- String]
        :query-params  [{recipient :- s/Uuid nil} {sender :- s/Uuid nil}]
        (cond
          (not (nil? recipient)) (get-notifications-by-recipient recipient authorization)
          (not (nil? sender))    (get-notifications-by-sender sender authorization)
          :else                  (get-all-notifications authorization)))
   (GET "/:id" {:as request}
        :auth-rules    {:or [admin? recipient? sender?]}
        :summary       ""
        :description   ""
        :header-params [authorization :- String]
        :path-params   [id :- Long]
        (get-notification id authorization))
   (POST "/" {:as request}
         :auth-rules    authenticated?
         :summary       ""
         :description   ""
         :header-params [authorization :- String]
         :body-params   [notification :- s/Any {recipients []}]
         (add-notification notification recipients authorization))
   (PUT "/:id" {:as request}
        :auth-rules    {:or [admin? sender?]}
        :summary       ""
        :description   ""
        :header-params [authorization :- String]
        :path-params   [id :- Long]
        :body-params   [fields :- s/Any]
        (update-notification id fields authorization))))

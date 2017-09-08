(ns api.routes.services.permissions
  "Routes for the Permissions API"
  (:require
   [api.auth.permission :as permission]
   [api.auth.permissions :refer [admin? read-only?]]
   [api.routes.core :refer [validate-and-respond]]
   [clojure.string :as str]
   [compojure.api.sweet :refer [context DELETE GET OPTIONS PATCH POST PUT]]
   [ring.util.http-response :as respond]
   [schema.core :as s]))

(defn activate-permission
  ""
  [id token]
  (validate-and-respond
   token
   #(respond/ok {:result (permission/activate id)})
   (str "Cannot activate PERMISSION " (or id "[undefined]"))))

(defn add-permission
  "Add a user permission."
  [fields token]
  (validate-and-respond
   token
   #(respond/ok {:result (permission/create fields)})
   (str "Cannot add PERMISSION " (or (:name fields) "[undefined]"))))

(defn deactivate-permission
  "Deactivates a permission by ID."
  [id token]
  (validate-and-respond
   token
   #(respond/ok {:result (permission/deactivate id)})
   (str "Cannot deactivate PERMISSION " (or id "[undefined]"))))

(defn delete-permission
  "Deletes permission completely."
  [id token]
  (validate-and-respond
   token
   #(respond/ok {:result (permission/delete id)})
   (str "Cannot delete PERMISSION " (or id "[undefined]"))))

(defn get-permission
  "Gets a permission by ID."
  [id token]
  (validate-and-respond
   token
   #(let [result (permission/retrieve-permission id)]
      (respond/ok result))
   (str "Unable to get PERMISSION " (or id "[undefined]"))))

(defn get-permissions
  "Gets all permissions."
  [token]
  (validate-and-respond
   token
   #(let [result (permission/retrieve-permissions)]
      (respond/ok result))
   "Unable to get PERMISSIONS"))

(defn update-permission
  "Updates a permission by given ID."
  [id fields token]
  (validate-and-respond
   token
   #(let [result (permission/modify id fields)]
      (respond/ok {:result result}))
   (str "Cannot update PERMISSION " (or id "[undefined]"))))

(def permission-context
  "Routes for permissions."
  (context "/api/permissions" []
           :auth-rules {:or [admin? read-only?]}
           :tags ["permissions"]
           (DELETE "/:id" {:as request}
                   :summary       ""
                   :description   ""
                   :header-params [authorization :- String]
                   :path-params   [id :- Long]
                   (delete-permission id authorization))
           (GET "/" {:as request}
                :summary       ""
                :description   ""
                :header-params [authorization :- String]
                (get-permissions authorization))
           (GET "/:id" {:as request}
                :summary       ""
                :description   ""
                :header-params [authorization :- String]
                :path-params   [id :- Long]
                (get-permission id authorization))
;;           (OPTIONS "/" {:as request}
;;                    :summary     ""
;;                    :description ""
;;                    (respond/ok {:get :post :patch :delete}))
           (PATCH "/:id" {:as request}
                  :summary       ""
                  :description   ""
                  :header-params [authorization :- String]
                  :path-params   [id :- Long]
                  :body-params   [fields :- s/Any]
                  (update-permission id fields authorization))
           (POST "/" {:as request}
                 :summary       ""
                 :description   ""
                 :header-params [authorization :- String]
                 :body-params   [fields :- s/Any]
                 (add-permission fields authorization))
           (PUT "/:id" {:as request}
                :summary       ""
                :description   ""
                :header-params [authorization :- String]
                :path-params   [id :- Long]
                :query-params  [{activate :- Boolean true}]
                (if (and (not (nil? activate)) activate)
                  (activate-permission id authorization)
                  (deactivate-permission id authorization)))))


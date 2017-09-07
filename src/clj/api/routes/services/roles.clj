(ns api.routes.services.roles
  "Routes for the Roles section"
  (:require
   [api.auth.permissions :refer [admin? read-only?]]
   [api.auth.role :as role]
   [api.routes.core :refer [validate-and-respond]]
   [clojure.set :refer [difference intersection]]
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [compojure.api.sweet :refer [context DELETE GET OPTIONS PATCH POST]]
   [ring.util.http-response :as respond]
   [schema.core :as s])
  (:import
   [javax.servlet.http HttpServletRequest HttpServletResponse]))

(defn activate-role
  ""
  [id token]
  (validate-and-respond
   token
   #(respond/ok {:result (role/activate id)})
   (str "Cannot activate ROLE "
        (or id "[undefined]"))))

(defn add-role
  "Add a user role."
  [fields token]
  (validate-and-respond
   token
   #(let [perm-ids (:permissions fields)
          result   (first (role/create (dissoc fields :permissions)))
          id       (:id result)
          result   (role/create-role-permissions id perm-ids)
          role     (role/find-one id)]
     (respond/ok role))
   (str "Cannot finish adding ROLE "
        (or (:name fields) "[undefined]"))))

(defn deactivate-role
  "Deactivates a role by ID."
  [id token]
  (validate-and-respond
   token
   #(respond/ok {:result (role/deactivate id)})
   (str "Cannot deactivate ROLE "
        (or id "[undefined]"))))

(defn delete-role
  "Deletes role completely."
  [id token]
  (validate-and-respond
   token
   #(respond/ok {:result (role/delete id)})
   (str "Cannot delete ROLE "
        (or id "[undefined]"))))

(defn get-role
  "Gets a role by ID."
  [id token]
  (validate-and-respond
   token
   #(respond/ok (role/find-one id))
   (str "Unable to get ROLE "
        (or id "[undefined]"))))

(defn get-role-permissions
  "Gets all permissions in a role by role ID."
  [id token]
  (validate-and-respond
   token
   #(respond/ok (role/find-permissions id))
   (str "Unable to get PERMISSIONS for ROLE "
        (or id "[undefined]"))))

(defn get-roles
  "Gets all roles."
  [token]
  (validate-and-respond
   token
   #(respond/ok (role/find-all))
   "Unable to get ROLES"))

(defn update-role
  "Updates a role by given ID."
  [id fields token]
  (validate-and-respond
   token
   #(let [perm-ids     (set (:permissions fields))
          old-perm-ids (into #{} (map (fn [x] (:id x)) (role/find-permissions id)))
          perm-ids     (difference perm-ids (intersection perm-ids old-perm-ids))
          old-perm-ids (difference old-perm-ids (intersection perm-ids old-perm-ids))
          _            (when (not (empty? old-perm-ids)) (role/delete-role-permissions id old-perm-ids))
          _            (when (not (empty? perm-ids)) (role/create-role-permissions id perm-ids))
          result       (role/modify id (dissoc fields :permissions))]
        (respond/ok {:result result}))
   (str "Cannot update ROLE " (or id "[undefined]"))))

;:query-params [active :- Boolean]
(def role-context
  "Routes for roles."
  (context "/api/roles" []
           :auth-rules {:or [admin? read-only?]}
           :tags ["roles"]
           (DELETE "/:id" {:as request}
                   :summary       ""
                   :description   ""
                   :header-params [authorization :- String]
                   :path-params   [id :- Long]
                   (delete-role id authorization))
           (GET "/" {:as request}
                :summary     ""
                :description ""
                :header-params [authorization :- String]
                (get-roles authorization))
           (GET "/:id" {:as request}
                :summary     ""
                :description ""
                :header-params [authorization :- String]
                :path-params [id :- Long]
                (get-role id authorization))
           (GET "/:id/permissions" {:as request}
                :summary     ""
                :description ""
                :header-params [authorization :- String]
                :path-params [id :- Long]
                (get-role-permissions id authorization))
;;           (OPTIONS "/" {:as request}
;;                    :summary     ""
;;                    :description ""
;;                    (respond/ok {:get :post :patch :delete}))
           (PATCH "/:id" {:as request}
                  :summary     ""
                  :description ""
                  :header-params [authorization :- String]
                  :path-params [id :- Long]
                  :body-params [fields :- s/Any]
                  (update-role id fields authorization))
           (POST "/" {:as request}
                 :summary     ""
                 :description ""
                 :header-params [authorization :- String]
                 :body-params [fields :- s/Any]
                 (add-role fields authorization))))

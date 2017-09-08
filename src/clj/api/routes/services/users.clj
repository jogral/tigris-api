(ns api.routes.services.users
  "Routes for the Users API"
  (:require
   [api.auth.permissions :refer [admin? login? read-only?]]
   [api.auth.user :as user]
   [api.middleware :refer [pubkey]]
   [api.platform.enrollments :as enroll]
   [api.routes.core :refer [respond-or-catch validate-and-respond]]
   [api.util.core :refer [generate-token refresh-token]]
   [buddy.auth :refer [authenticated?]]
   [buddy.sign.jwt :as jwt]
   [cheshire.core :as json]
   [clojure.string :as str]
   [compojure.api.sweet :refer [context DELETE GET OPTIONS PATCH POST PUT]]
   [ring.util.http-response :as respond]
   [schema.core :as s]
   api.routes.restructure))

(defn add-user
  [fields role token]
  (validate-and-respond
   token
   #(let [result (first (user/create fields))
          _      (user/add-to-role (:id result) role)]
      (respond/ok {:result result}))
   (str "Cannot add user " (or (:email fields) "[undefined]"))))

(defn add-user-enrollment
  [id e token]
  (validate-and-respond
   token
   #(let [result (enroll/create-enrollment id (:course_id e) (:progress e))
          result (if (nil? result)
                   {:error {:code 110 :message "ENROLLMENT already exists."}}
                   result)]
      (respond/ok {:result result}))
   "Could not add ENROLLMENT."))

(defn authenticate-user
  [username password]
  (respond-or-catch
   #(let [result (user/authenticate username password)
          user?  (not (nil? result))
          admin? (user/admin? (:id result))
          token  (cond
                   user? (generate-token result)
                   :else nil)]
      (assoc-in
       (respond/ok {:admin admin? :user result :token token})
       [:session :identity] {:token token}))
   "Could not attempt authentication."))

(defn delete-user
  [id token]
  (validate-and-respond
   token
   #(let [result (user/delete id)]
      (respond/ok {:result result}))
   "Cannot delete user."))

(defn delete-user-enrollment
  ""
  [id user-id token]
  (validate-and-respond
   token
   #(let [result (enroll/destroy-enrollment id user-id)]
      (respond/ok {:result result}))
   (str "Could not delete ENROLLMENT " id ".")))

(defn get-user
  [id token]
  (validate-and-respond
   token
   #(let [result (user/find-one id)]
      (respond/ok result))
   "Cannot get user."))

(defn get-user-by-col
  "Gets user by column."
  [col val token]
  (validate-and-respond
   token
   #(respond/ok (user/find-one-by-col col val))
   (str "Cannot get user by " (.toUpperCase col) " " val)))

(defn get-user-enrollment
  [id user-id token]
  (validate-and-respond
   token
   #(let [result (enroll/retrieve-enrollment id user-id)]
      (respond/ok result))
   (str "Could not get ENROLLMENT " id ".")))

(defn get-user-enrollments-for-course
  ""
  [user-id course-id token]
  (validate-and-respond
   token
   #(let [result (enroll/retrieve-enrollments-by-user-for-course user-id course-id)]
      (respond/ok result))
   (str "Could not get ENROLLMENTs for USER in COURSE " course-id ".")))

(defn get-user-roles
  ""
  [id token]
  (validate-and-respond
   token
   #(let [result (user/user-roles id)]
      (respond/ok result))
   "Cannot get USER ROLEs."))

(defn get-users
  [token]
  (validate-and-respond
   token
   #(respond/ok (user/find-all))
   "Cannot get user."))

(defn refresh-user
  "Refresh the user JWT."
  [id token]
  (validate-and-respond
   token
   #(respond/ok (assoc-in
                 {:user   (user/find-one id)
                  :admin  (user/admin? id)
                  :token  (refresh-token token)}
                 [:session :identity] {:token token}))
   "Could not attempt re-authentication."))

(defn update-user
  [action id fields roles token]
  (validate-and-respond
   token
   #(let [action   (str/lower-case action)
          result   (cond
                     (= action "activate")   (user/activate id)
                     (= action "deactivate") (user/deactivate id)
                     :else                   (user/modify id fields))
          success? (if (nil? roles)
                     true
                     (first (user/add-to-role id (first roles))))] ;; TODO: Eventually users can be in 2 groups.
      (respond/ok {:result result :success? success?}))
   (str "Cannot update user " (or id "[undefined]"))))

(defn update-user-enrollment
  [enrollment-id user-id enrollment token]
  (validate-and-respond
   token
   #(let [result (enroll/update-enrollment enrollment-id user-id enrollment)]
      (respond/ok {:result result}))
   "Could not update ENROLLMENT."))

(defn change-user-enrollment
  ""
  [enrollment-id user-id enroll? token]
  (validate-and-respond
   token
   #(let [result (if enroll?
                   (enroll/start-enrollment enrollment-id user-id)
                   (enroll/end-enrollment enrollment-id user-id))]
      (respond/ok {:result result}))
   "Could not change ENROLLMENT."))

(defn complete-user-enrollment
  ""
  [enrollment-id user-id token]
  (validate-and-respond
   token
   #(let [result (enroll/complete-enrollment enrollment-id user-id)]
      (respond/ok {:result result}))
   "Could not complete ENROLLMENT."))

(def user-context
  "Routes for DB users."
  (context "/api/users" []
           :auth-rules {:or [admin? login? read-only?]}
           :tags ["user"]
           (DELETE "/:id" {:as request}
                   :summary       ""
                   :description   ""
                   :header-params [authorization :- String]
                   :path-params   [id :- s/Uuid]
                   (delete-user id authorization))
           (DELETE "/:user-id/enrollments/:enrollment-id" {:as request}
                   :summary       ""
                   :description   ""
                   :header-params [authorization :- String]
                   :path-params   [user-id :- s/Uuid enrollment-id :- Long]
                (delete-user-enrollment enrollment-id user-id authorization))
           (GET "/" {:as request}
                :summary       ""
                :description   ""
                :header-params [authorization :- String]
                :query-params  [{shortname :- String nil}{email :- String nil}]
                (cond
                  (not (nil? shortname)) (get-user-by-col "shortname" shortname authorization)
                  (not (nil? email))     (get-user-by-col "email" email authorization)
                  :else                  (get-users authorization)))
           (GET "/:id" {:as request}
                :summary       ""
                :description   ""
                :header-params [authorization :- String]
                :path-params   [id :- s/Uuid]
                (get-user id authorization))
           (GET "/:id/roles" {:as request}
                :summary       ""
                :description   ""
                :header-params [authorization :- String]
                :path-params   [id :- s/Uuid]
                (get-user-roles id authorization))
           (GET "/:user-id/enrollments/:enrollment-id" {:as request}
                :summary       ""
                :description   ""
                :header-params [authorization :- String]
                :path-params   [user-id :- s/Uuid enrollment-id :- Long]
                (get-user-enrollment enrollment-id user-id authorization))
;;           (OPTIONS "/" {:as request}
;;                    :summary       ""
;;                    :description   ""
;;                    (respond/ok {:get :post :patch :delete}))
           (PATCH "/:id" {:as request}
                  :summary       ""
                  :description   ""
                  :header-params [authorization :- String]
                  :path-params   [id :- s/Uuid]
                  :body-params   [fields :- s/Any action :- String {roles :- s/Any nil}]
                  (update-user action id fields roles authorization))
           (PATCH "/:user-id/enrollments/:enrollment-id" {:as request}
                :summary       ""
                :description   ""
                :header-params [authorization :- String]
                :path-params   [user-id :- s/Uuid enrollment-id :- Long]
                :body-params   [fields :- s/Any]
                :query-params  [{action :- String nil}]
                (cond
                  (and (not (nil? action))
                       (= (.toLowerCase action) "enroll"))   (change-user-enrollment enrollment-id
                                                                                     user-id
                                                                                     true
                                                                                     authorization)
                  (and (not (nil? action))
                       (= (.toLowerCase action) "unenroll")) (change-user-enrollment enrollment-id
                                                                                     user-id
                                                                                     false
                                                                                     authorization)
                  (and (not (nil? action))
                       (= (.toLowerCase action) "complete")) (complete-user-enrollment enrollment-id
                                                                                       user-id
                                                                                       authorization)
                  :else                                      (update-user-enrollment enrollment-id
                                                                                     user-id
                                                                                     fields
                                                                                     authorization)))
           (POST "/" {:as request}
                 :summary       ""
                 :description   ""
                 :header-params [authorization :- String]
                 :body-params   [fields :- s/Any role :- Long]
                 (add-user fields role authorization))
           (POST "/:id/enrollments" {:as request}
                :summary       ""
                :description   ""
                :header-params [authorization :- String]
                :path-params   [id :- s/Uuid]
                :body-params   [fields :- s/Any]
                (add-user-enrollment id fields authorization))
           (POST "/logout" {:as request}
                 :return String
                 :summary     "Logout user session."
                 :description "Kills a user session."
                 (assoc (respond/ok "ok") :session nil))
           (POST "/authenticate" {:as request}
                 :summary     "Authenticate a user."
                 :description "Authenticates a user against the DB."
                 :body-params [username :- String password :- String]
                 (authenticate-user username password))
           (PUT "/refresh" {:as request}
                  :summary     "Refresh the user."
                  :description "Refresh the user token."
                  :header-params [authorization :- String]
                  :body-params   [id :- s/Uuid]
                  (refresh-user id authorization))))


(ns api.routes.services.utils
  "Utility routes."
  (:require
   [api.auth.user :as user]
   [api.routes.core :refer [respond-or-catch validate-and-respond]]
   [api.util.core :as util]
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [compojure.api.sweet :refer [context DELETE GET PATCH POST PUT]]
   [ring.util.http-response :as respond]
   [schema.core :as s]
   [sendgrid.core :as sg]
   [slugify.core :refer [slugify]]))
   

;; TODO: Move to config file or env vars
(def api-token "Bearer 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")

(defn invitation-token
  "Creates an invitation token."
  [id email token]
  (validate-and-respond
   token
   #(respond/ok {:token (util/generate-invitation-token id email)})
   "Cannot create token."))

(defn send-email-via-sg
  "Sends an e-mail using SendGrid service"
  [to subject message token]
  (validate-and-respond
   token
   #(let [_ (sg/send-email {:api-token api-token
                            :from "no-reply@jogral.io"
                            :to to
                            :subject subject
                            :message message})]
      (respond/ok {:result "OK"}))
   "Cannot send email."))

(defn slugify-value
  ""
  [val token]
  (validate-and-respond
   token
   #(respond/ok {:result (slugify val)})
   (str "Cannot slugify string " val)))

(defn validate-token
  "Validates JWT."
  [token]
  (respond-or-catch
   #(respond/ok (util/validate-invitation-token token))
   "Cannot validate token."))

(defn finalize-user
  "Finishes user registration."
  [id password]
  (respond-or-catch
   #(let [result (user/modify id {:password password})]
    (respond/ok {:result result}))
  "Cannot finalize USER."))

(defn reset-password-token
  "Creates an invitation token."
  [email message]
  (respond-or-catch
   #(let [user  (user/find-one-by-col "email" email)
          token (util/generate-invitation-token (:id user) email 8)
          _     (when (not (nil? user))
                  (sg/send-email {:api-token api-token
                                  :from "no-reply@jogral.io"
                                  :to (:email user)
                                  :subject (:subject message)
                                  :message (str/replace (:body message) #"\[token\]" token)}))]
     (respond/ok {:token token}))
   "Cannot create token."))

(def util-context
  ""
  (context
   "/api/utils"
   []
   :tags ["util"]
   (GET "/token/:token" {:as request}
        :summary     "Validates token."
        :description "Validates a token generated via email."
        :path-params [token :- String]
        (validate-token token))
   (POST "/token" {:as request}
         :summary       "Creates a token for inviting a user."
         :description   "Creates a JWT to use for creating a new user."
         :header-params [authorization :- String]
         :body-params   [id :- s/Uuid email :- String]
         (invitation-token id email authorization))
   (POST "/reset-password" {:as request}
         :summary     "Resets a password."
         :description "Creates a JWT to reset a password."
         :body-params [email :- String message :- s/Any]
         (reset-password-token email message))
   (POST "/send-email" {:as request}
         :summary       "Sends an email."
         :description   "Sends an email with to, subject, and message provided."
         :header-params [authorization :- String]
         :body-params   [to :- String subject :- String message :- String]
         (send-email-via-sg to subject message authorization))
   (POST "/finalize" {:as request}
         :summary "Finalizes user creation."
         :description "Finalizes the user flow."
         :body-params [id :- s/Uuid password :- String]
         (finalize-user id password))
   (PUT "/slugify" {:as request}
         :summary       "Turns string into a slug."
         :description   "This takes a string, presumably free-text, and formats it to be URL-friendly."
         :header-params [authorization :- String]
         :body-params   [val :- String]
         (slugify-value val authorization))))

(ns api.routes.services.saml
  "Routes for SAML SSO."
  (:require
   [api.auth.saml :as saml]
   [api.routes.core :refer [respond-or-catch]]
   [clojure.string :as str]
   [compojure.api.sweet :refer [context DELETE GET OPTIONS PATCH POST]]
   [ring.util.http-response :as respond]
   [schema.core :as s]))

(defn metadata
  ""
  []
  (respond-or-catch
   #(respond/ok (saml/metadata))
   (str "Cannot get SAML metadata.")))

(defn acs
  ""
  [request]
  (respond-or-catch
   #(respond/ok (saml/assertion-consumer-service request (respond/ok)))
   (str "Failed in getting assertion consumer service.")))

(defn attrs
  ""
  [{session :session}]
  (let [elems (keys session)]
    (if-let [found (or (contains? elems "attributes") (contains? elems "nameId"))]
      (respond/ok {:attributes (:attributes session) :name-id (:nameId session)})
      (respond/ok {:attributes "" :name-id ""}))))

(def saml-context
  "Routes for SAML SSO."
  (context "/api/saml" []
           :tags ["saml"]
           (GET "/acs" {:as request}
                :summary     ""
                :description ""
                (acs request))
           (GET "/attrs" {:as request}
                :summary     ""
                :description ""
                (attrs :session))
           (GET "/metadata" {:as request}
                :summary     "Gets service provider metadata."
                :description "Gets service provider metadata."
                (respond/content-type
                 (metadata)
                 "text/xml"))))

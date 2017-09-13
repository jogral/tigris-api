(ns api.routes.xapi
  (:require
   [api.auth.permissions :refer [admin?]]
   [api.routes.core :refer [validate-and-respond]]
   [api.service.xapi :as xapi]
   [compojure.api.sweet :refer [context POST]]
   [ring.util.http-response :as respond]
   [schema.core :as s]))

(defn crap
  []
  true)

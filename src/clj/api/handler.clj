(ns api.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [api.layout :refer [error-page]]
            [api.routes.home :refer [home-routes]]
            [api.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [api.config :refer [env]]
            [api.env :refer [defaults]]
            [mount.core :as mount]
            [api.middleware :as middleware]
            [clojure.tools.logging :as log]
            [ring.middleware.cors :refer [wrap-cors]]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (wrap-cors #'service-routes
               :access-control-allow-origin [#"http://localhost:8080"] ;(env :allowed-domains)
               :access-control-allow-methods [:get :patch :post :put :delete :options])
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))

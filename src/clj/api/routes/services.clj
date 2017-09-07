(ns api.routes.services
  (:require [api.routes.services.assessments :refer [quiz-context test-context]]
            [api.routes.services.courses :refer [course-context]]
            [api.routes.services.notifications :refer [notification-context]]
            [api.routes.services.permissions :refer [permission-context]]
            [api.routes.services.roles :refer [role-context]]
            [api.routes.services.users :refer [user-context]]
            [api.routes.services.utils :refer [util-context]]
            [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))

(defapi service-routes
  {:swagger {:ui "/api"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Tigris API"
                           :description "API for the Tigris back end"}}}}
  ;                  :tags [{:name "api", :description "tigris apis"}]}}}
  course-context
  notification-context
  permission-context
  quiz-context
  role-context
  test-context
  user-context
  util-context)
